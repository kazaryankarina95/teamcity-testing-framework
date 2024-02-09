package com.example.teamcity.ui;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.element;

public class CreateNewUiProjectTest extends BaseUiTest {

    // ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION ON UI USE CASE ************

    @Test
    // Main positive test: project ID with all allowed characters (latin alphanumeric, underscore, starts from latin later) is created.
    public void authorizedUserShouldBeAbleToCreateProject() {
        var testData = TestDataStorage.addTestData();
        loginAsUser(testData.getUser());

        var expectedProjectName = testData.getProject().getName();
        testData.getProject().setId(expectedProjectName);
        var expectedProjectId = testData.getProject().getId();
        System.out.println("Expected project ID before creation is " + expectedProjectId);

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(expectedProjectName, testData.getBuildType().getName());

        SelenideElement projectWasCreated = element(Selectors.byId("unprocessed_objectsCreated"));
        String expectedText = String.format("New project \"%s\", build configuration \"%s\" and VCS root \"%s\" have been successfully created.", testData.getProject().getName(), testData.getBuildType().getName(), GIT_URL + "#refs/heads/master");
        projectWasCreated.shouldHave(text(expectedText));

        new ProjectsPage().open().getSubprojects();

        SelenideElement currentProject = element(Selectors.byDataTest("subproject"));
        currentProject.shouldHave(text(expectedProjectName));

        var fetchedProjectViaApi = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser())).get(Locator.BY_NAME, expectedProjectName);
        softy.assertThat(expectedProjectName).isEqualTo(fetchedProjectViaApi.getName());

        var nameOfFetchedProjectViaApi = fetchedProjectViaApi.getName();
        nameOfFetchedProjectViaApi = Character.toLowerCase(nameOfFetchedProjectViaApi.charAt(0)) + nameOfFetchedProjectViaApi.substring(1);
        if (nameOfFetchedProjectViaApi.charAt(4) != '_') { // Check if there already is an underscore at the fourth position, and if not, add it after the 4th letter
            nameOfFetchedProjectViaApi = nameOfFetchedProjectViaApi.substring(0, 4) + "_" + nameOfFetchedProjectViaApi.substring(4);
        }
        System.out.println("The name of fetched project via API with 1st lower case letter and underscore after the 4th letter " + nameOfFetchedProjectViaApi);
        softy.assertThat(expectedProjectId).isEqualTo(nameOfFetchedProjectViaApi);
    }

    @Test
    // Boundary values test: max characters limit for project ID (255 characters)
    // Actually there are no limitations on UI, project can be created with ANY ID
    public void authorizedUserShouldBeAbleToCreateProjectWith255CharactersName() {
        var testData = TestDataStorage.addTestData();
        loginAsUser(testData.getUser());

        String longNumber225 = RandomData.getString225();
        testData.getProject().setName(longNumber225);
        var expectedProjectName = testData.getProject().getName();

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement projectWasCreated = element(Selectors.byId("unprocessed_objectsCreated"));
        String expectedText = String.format("New project \"%s\", build configuration \"%s\" and VCS root \"%s\" have been successfully created.", testData.getProject().getName(), testData.getBuildType().getName(), GIT_URL + "#refs/heads/master");
        projectWasCreated.shouldHave(text(expectedText));

        new ProjectsPage().open().getSubprojects();

        SelenideElement currentProject = element(Selectors.byDataTest("subproject"));
        currentProject.shouldHave(text(testData.getProject().getName()));

        var fetchedProjectViaApi = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser())).get(Locator.BY_NAME, expectedProjectName);
        softy.assertThat(expectedProjectName).isEqualTo(fetchedProjectViaApi.getName());
    }

    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR PROJECT CREATION ON UI USE CASE ************

    @Test
    public void authorizedUserShouldNotBeAbleToCreateProjectWithEmptyName() {
        var testData = TestDataStorage.addTestData();
        loginAsUser(testData.getUser());

        testData.getProject().setName(" ");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement errorMessage = element(Selectors.byId("error_projectName"));
        errorMessage.shouldHave(text("Project name must not be empty"));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateProjectWithSameName() {
        var testData = TestDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        testData.getBuildType().setName(RandomData.getString());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement errorMessage = element(Selectors.byId("error_projectName"));
        errorMessage.shouldHave(text("Project with this name already exists: " + testData.getProject().getName()));
    }
}


