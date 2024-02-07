# in order to run our script from root but not from "infra" folder, we have to go one directory up
cd ..

# current directory = pwd
teamcity_tests_directory=$(pwd)

#we have to create a folder, where we are going to run server and selenoid from. Logs will be also stored here.
workdir="teamcity_tests_infrastructure"
# folders, where we will run teamcity server and teamcity agent, and Selenoid. They all belong to workdir="teamcity_tests_infrastructure"
teamcity_server_workdir="teamcity_server"
teamcity_agent_workdir="teamcity_agent"
selenoid_workdir="selenoid"

# names for containers to be able to clean data by containers name
teamcity_server_container_name="teamcity_server_instance"
teamcity_agent_container_name="teamcity_agent_instance"
selenoid_container_name="selenoid_instance"
selenoid_ui_container_name="selenoid_ui_instance"

# List of workdirs
workdir_names=($teamcity_server_workdir $teamcity_agent_workdir $selenoid_workdir)
# list of containes
container_names=($teamcity_server_container_name $teamcity_agent_container_name $selenoid_container_name $selenoid_ui_container_name)

##############################################
# echo prints the result of a specific command into terminal
echo "Request IP"
export ip=$(ipconfig getifaddr en0)
# export ip={$ips%%$'\n'*} - this command can be used in case you need to get the 1st IP from the list of IPs you've got in the previous command
echo "Current IP: $ip"
# we want to create the list of containers and go through this list to delete all of them one by one. variable "container_names" will help here

##############################################
echo "Delete previous run data"
# clean directory (removing previously created folder to run tests in clean env)
rm -rf $workdir
mkdir $workdir
cd $workdir
# for each directory int he list, create a new directory
for dir in "${workdir_names[@]}"; do
mkdir $dir
done

# for each container int he list, stop Docker, and delete container
for container in "${container_names[@]}"; do
  docker stop $container
  docker rm $container
done

##############################################
echo "Start teamcity server"
cd $teamcity_server_workdir
# this command was taken from https://github.com/JetBrains/teamcity-docker-images/blob/master/dockerhub/teamcity-server/README.md
docker run -d --name $teamcity_server_container_name  \
    -v $(pwd)/logs:/opt/teamcity/logs  \
    -p 8111:8111 \
    jetbrains/teamcity-server

echo "Teamcity Server is running..."

##############################################
echo "Start teamcity agent"
cd $teamcity_agent_workdir
# this command was taken from https://hub.docker.com/r/jetbrains/teamcity-agent/
docker run -d --name $teamcity_agent_container_name  \
    -e SERVER_URL="http://$ip:8111"
    -v $(pwd)/conf:/data/teamcity_agent/conf  \
    jetbrains/teamcity-agent

echo "Teamcity Agent is running..."

##############################################
# prior starting Selenoid we have to pass the list of browsers in json format
echo "Start Selenoid"

cd .. && cd $selenoid_workdir
mkdir config
cp $teamcity_tests_directory/infra/browsers.json config/

docker run -d                                   \
            --name $selenoid_container_name                                 \
            -p 4444:4444                                    \
            -v /var/run/docker.sock:/var/run/docker.sock    \
            -v $(pwd)/config/:/etc/selenoid/:ro              \
    aerokube/selenoid:latest-release

# we have to parse all browsers that we have in our browsers.json. We can ask ChatGPT "How to parse regexp "image: "value"". We got the command:
image_names=($(awk -F'"' '/"image": "/{print $4}' "$(pwd)/config/browsers.json"))

echo "Pull all browser images: $image_names"

for image in "${image_names[@]}"; do
  docker pull $image
done

##############################################
echo "Start selenoid-ui"
docker run -d --name selenoid-ui -p 8080:8080 aerokube/selenoid-ui --selenoid-uri http://$ip:4444

cd .. && cd ..

##############################################
echo "Set up teamcity server"
mvn clean test -Dtest=SetupTest#startUpTest

##############################################
echo "Parse superuser token"
# we have to parse superuser token from teamcity-server > logs >teamcity-server.log. Will be similar to what we did with parsing image names. With regexp or grep command
superuser_token=$(grep -o 'Super user authentication token: [0-9]*' $teamcity_tests_directory/$workdir/$teamcity_server_workdir/logs/teamcity-server.log | awk '{print $NF}')
echo "Super user token: $superuser_token"

##############################################
echo "Run system tests"

echo "host=$ip:8111\nsuperUserToken=$superuser_token\nremote=true" > $teamcity_tests_directory/src/main/resources/config.properties
cat $teamcity_tests_directory/src/main/resources/config.properties

echo "Run API tests"
mvn test -DsuiteXmlFile=$testing-suites/api.suite.xml

echo "Run UI tests"
mvn test -DsuiteXmlFile=$testing-suites/ui.suite.xml


