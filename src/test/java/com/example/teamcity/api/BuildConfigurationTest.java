package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_WITH_SAME_NAME_ALREADY_EXISTS;
import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_CANT_BE_CREATED_WITH_PROJECT_NULL;
import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_CANT_HAVE_ID_226_CHARACTERS;
import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_CANT_HAVE_INVALID_ID;
import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_ID_IS_USED;
import static com.example.teamcity.api.errors.Errors.BUILD_CONFIG_NAME_SHOULD_BE_PROVIDED;
import static com.example.teamcity.api.generators.RandomData.generateStringWithSpecialCharacters;

@Epic("API tests")
@Feature("Build configuration")
@Story("Creating build config")
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
        testData.getBuildType().setId(longNumber225);

        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

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

        String nameWithSpecialCharacters = generateStringWithSpecialCharacters(40);
        testData.getBuildType().setName(nameWithSpecialCharacters);

        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

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

        testData.getBuildType().setId(null);
        // We create a new project with id=null
        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

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
                .body(Matchers.containsString(String.format(BUILD_CONFIG_WITH_SAME_NAME_ALREADY_EXISTS, buildConfigName, project.getName())));
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
                .body(Matchers.containsString(String.format(BUILD_CONFIG_ID_IS_USED, buildConfigId)));
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

        testData.getBuildType().setName(null);

        // We create a new project with name=null
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(BUILD_CONFIG_NAME_SHOULD_BE_PROVIDED));
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

        testData.getBuildType().setProject(null);

        // We create a new project with project=null
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(BUILD_CONFIG_CANT_BE_CREATED_WITH_PROJECT_NULL));
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
        testData.getBuildType().setId(InvalidId);

        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString(String.format(BUILD_CONFIG_CANT_HAVE_INVALID_ID, InvalidId)));
    }

    @Test
    // Boundary values test: project ID (256 characters) > max characters limit (255 characters) (500 status code)
    public void unsuccessfulBuildConfigurationWithId256CharactersTest() {
        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        String longNumber226 = RandomData.getString226();
        testData.getBuildType().setId(longNumber226);

        // We create a new build configuration with ID containing 256 characters
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                // ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).
                .body(Matchers.containsString(String.format(BUILD_CONFIG_CANT_HAVE_ID_226_CHARACTERS, longNumber226)));
    }
}
