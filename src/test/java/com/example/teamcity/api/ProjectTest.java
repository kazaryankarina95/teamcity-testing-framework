package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {

    // ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION USE CASE ************

    @Test
    // Main positive test: project ID with all allowed characters (latin alphanumeric, underscore, starts from latin later) is created. Note: I`ve modified getString() method in RandomData class for it co contain digits as well
    public void successfulProjectCreationTest() {

        // We generate data for system admin/project/build config and add it into test data storage
        var testData = testDataStorage.addTestData();

        // As a system admin we create a new valid user
        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with all required fields
        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    // Boundary values test: max characters limit for project ID (255 characters)
    public void successfulProjectCreationWithId255CharactersTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with boundary value of project id
        String longNumber225 = RandomData.getString225();
        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(RandomData.getString())
                        .id(longNumber225)
                        .copyAllAssociatedSettings(true)
                        .build());
        softy.assertThat(project.getId()).isEqualTo(longNumber225);
    }

    @Test
    // Project with all possible characters in Project Name
    public void successfulProjectCreationWithAllAllowedCharactersTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        String nameWithSpecialCharacters = RandomData.generateStringWithSpecialCharacters(40);

       var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(nameWithSpecialCharacters)
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(true)
                        .build());

        softy.assertThat(project.getName()).isEqualTo(nameWithSpecialCharacters);
    }

    @Test
    // parentProject will be "_Root" by default, in case parentProject=null
    public void successfulProjectCreationWithNullParentProjectTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with name=null
        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(null)
                        .name(RandomData.getString())
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(true)
                        .build());

        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());

    }

    @Test
    // Project with copyAllAssociatedSettings=false
    public void unsuccessfulProjectCreationWithAssociatedSettingsFalseTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with empty project ID
        var project = new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(RandomData.getString())
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(false)
                        .build());

        softy.assertThat(project.equals(project));
    }

    @Test
    /* Project can`t have id=null, however, if you don`t pass id, TeamCity takes it from ProjectName and modify it
     I left this case here to show my thoughts. Unfortunately, I couldn't figure out how to modify ProjectName as TeamCity does
     TeamCity takes ProjectName, changes the first letter to an opposite case letter (in our case it`s an upper-case letter), and remove underscore.
     Example: ProjectId = "TestEpsMS22912" because ProjectName was test_epsMS22912
     So, if you send empty ProjectId, it will be taken from ProjectName
     */
    public void successfulProjectCreationWithIdNullTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with id=null
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(RandomData.getString())
                        .id(null)
                        .copyAllAssociatedSettings(true)
                        .build());

        //  softy.assertThat(buildType.getId()).isEqualTo(testData.getProject().getName());
    }


    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR PROJECT CREATION USE CASE ************

    @Test
    // projects can`t have the same name (400 status code)
    public void unsuccessfulProjectCreationWithSameNameTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a project with the same name
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists"));
    }

    @Test
    // projects can`t have the same id (400 status code)
    public void unsuccessfulProjectCreationWithSameIdTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // we set the project name that will be used in 2nd project creation
        testData.getProject().setName(RandomData.getString());

        // We create a project with the same ID
        var project = new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(String.format("Project ID \"%s\" is already used by another project", testData.getProject().getId())));
    }

    //
    @Test
    // Project can`t have wrong project Root (404 status code)
    public void unsuccessfulProjectCreationWithWrongParentRootTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        String Root = RandomData.getString();
        // We create a new project with wrong parent root
        var projectWithWrongRoot = new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator(Root)
                                .build())
                        .name(RandomData.getString())
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(true)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by name or internal/external id " + "'" + Root + "'"));
    }

    @Test
    // Project can`t be created with name=null (400 status code)
    public void unsuccessfulProjectCreationWithNullNameTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with name=null
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(null)
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(true)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));
    }

    @Test
    // Projects can`t be created with empty project ID (500 status code)
    public void unsuccessfulProjectCreationWithEmptyIdTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with empty project ID
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(RandomData.getString())
                        .id(" ")
                        .copyAllAssociatedSettings(true)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("Project ID must not be empty."));
    }

    @Test
    // Projects can`t be created with empty project name (500 status code)
    public void unsuccessfulProjectCreationWithEmptyNameTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with empty project ID
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(" ")
                        .id(RandomData.getString())
                        .copyAllAssociatedSettings(true)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("Given project name is empty."));
    }

    @Test
    // Boundary values test: project ID (256 characters) > max characters limit (255 characters) (500 status code)
    public void unsuccessfulProjectCreationWithSpecialSymbolInNameTest() {

        var testData = testDataStorage.addTestData();

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with project ID containing 256 characters
        String longNumber226 = RandomData.getString226();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(NewProjectDescription
                        .builder()
                        .parentProject(Project.builder()
                                .locator("_Root")
                                .build())
                        .name(RandomData.getString())
                        .id(longNumber226)
                        .copyAllAssociatedSettings(true)
                        .build())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                //ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).
                .body(Matchers.containsString(String.format("Project ID \"%s\" is invalid: it is 226 characters long while the maximum length is 225.", longNumber226)));
    }
}
