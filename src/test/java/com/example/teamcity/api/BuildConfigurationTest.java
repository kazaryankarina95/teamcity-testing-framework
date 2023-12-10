package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {

    // ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION USE CASE ************

    @Test
    // Positive test: build config with all allowed characters (latin alphanumeric, underscore, starts from latin later) in project ID is created. Note: I`ve modified getString() method in RandomData class
    public void successfulBuildConfigurationTest() {

        // We generate data for system admin/project/build config and add it into test data storage
        var testData = testDataStorage.addTestData();

        // As a system admin we create a new valid user
        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with all required fields
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a new build config with all required fields
        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    // Boundary values test: max characters limit for build config ID (255 characters)
    public void successfulBuildConfigurationWithId255CharactersTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        String longNumber225 = RandomData.getString225();

        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(longNumber225)
                        .name(RandomData.getString())
                        .project(testData.getProject())
                        .build());

        softy.assertThat(buildType.getId()).isEqualTo(longNumber225);
    }

    @Test
    // Project with all possible characters in Build Config Name
    public void successfulBuildConfigurationWithAllAllowedCharactersTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        String nameWithSpecialCharacters = RandomData.generateStringWithSpecialCharacters(40);

        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(RandomData.getString())
                        .name(nameWithSpecialCharacters)
                        .project(testData.getProject())
                        .build());

        softy.assertThat(buildType.getName()).isEqualTo(nameWithSpecialCharacters);
    }

    @Test
    /* Build config can`t have id=null, however, if you don`t pass id, TeamCity makes it from ProjectId + modified BuildTypeName
     I left this case here to show my thoughts. Unfortunately, I couldn't figure out how to modify BuildTypeName as TeamCity does
     TeamCity takes BuildTypeName, changes the first letter to an opposite case letter (in our case it`s an upper-case letter), and remove underscore.
     Example: BuildTypeId = "test_AhoPM89321_TestJJlFz07607" because ProjectId was test_AhoPM89321 and BuildTypeName was test_JJlFz07607
     So, if you send empty BuildTypeId, it will be taken as a combination of ProjectId and BuildTypeName
     */
    public void unsuccessfulBuildConfigurationWithIdNullTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a new project with id=null
        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(null)
                        .name(RandomData.getString())
                        .project(testData.getProject())
                        .build());

    //    softy.assertThat(buildType.getId()).isEqualTo(testData.getProject().getId() + testData.getBuildType().getName());

    }


    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR PROJECT CREATION USE CASE ************

    @Test
    // Build config can`t have the same name (400 status code)
    public void unsuccessfulBuildConfigurationWithSameNameTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

       var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        // we set the project name that will be used in 2nd project creation
        testData.getBuildType().setId(RandomData.getString());
        var buildConfigName = testData.getBuildType().getName();

        // We create a project with the same name
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format("Build configuration with name \"%s\" already exists in project: \"%s\"", buildConfigName, project.getName())));
    }

    @Test
    // Build config can`t have the same ID (400 status code)
    public void unsuccessfulBuildConfigurationWithSameIdTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        var buildConfigId = testData.getBuildType().getId();

        // We create a project with the same ID
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format("The build configuration / template ID \"%s\" is already used by another configuration or template", buildConfigId)));
    }

    @Test
    // Build config can`t have name=null (400 status code)
    public void unsuccessfulBuildConfigurationWithNameNullTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a new project with name=null
        var buildType = new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(RandomData.getString())
                        .name(null)
                        .project(testData.getProject())
                        .build())
        .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(Matchers.containsString("When creating a build type, non empty name should be provided."));

    }

    @Test
    // Build config can`t have project=null (400 status code)
    public void unsuccessfulBuildConfigurationWithProjectNullTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a new project with project=null
        var buildType = new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(RandomData.getString())
                        .name(RandomData.getString())
                        .project(null)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Build type creation request should contain project node."));

    }

    @Test
    // Build config with invalid ID. ID should start with a latin letter and contain only latin letters, digits and underscores (500 status code)
    public void unsuccessfulBuildConfigurationWith1CharacterInIdNullTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        String InvalidId = " 1&@дл";

        var buildType = new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        // I know that here should be data generation with the rule: "use ANY character besides latin letters, digits, underscore AND start with anything besides latin letter". I'll improve later
                        .id(InvalidId)
                        .name(RandomData.getString())
                        .project(testData.getProject())
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString(String.format("Build configuration or template ID \"%s\" is invalid: starts with non-letter character ' '. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).", InvalidId)));

    }
    @Test
    // Boundary values test: project ID (256 characters) > max characters limit (255 characters) (500 status code)
    public void unsuccessfulBuildConfigurationWithId256CharactersTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        String longNumber226 = RandomData.getString226();

        // We create a new build configuration with ID containing 256 characters
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(BuildType.builder()
                        .id(longNumber226)
                        .name(RandomData.getString())
                        .project(testData.getProject())
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                // ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).
                .body(Matchers.containsString(String.format("Build configuration or template ID \"%s\" is invalid: it is 226 characters long while the maximum length is 225.", longNumber226)));
    }
}
