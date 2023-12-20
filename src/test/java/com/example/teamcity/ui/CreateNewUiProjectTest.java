package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.assertj.core.api.StringAssert;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.element;
import static java.lang.String.*;

// ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION ON UI USE CASE ************

public class CreateNewUiProjectTest extends BaseUiTest {
    @Test
    // Main positive test: project ID with all allowed characters (latin alphanumeric, underscore, starts from latin later) is created.
    public void authorizedUserShouldBeAbleToCreateProject() {
        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement projectWasCreated = element(Selectors.byId("unprocessed_objectsCreated"));
        String expectedText = String.format("New project \"%s\", build configuration \"%s\" and VCS root \"%s\" have been successfully created.", testData.getProject().getName(), testData.getBuildType().getName(), url + "#refs/heads/master");
        projectWasCreated.shouldHave(text(expectedText));

        new ProjectsPage().open().getSubprojects();

        SelenideElement currentProject = element(Selectors.byClass("Subproject__entity--nm"));
        currentProject.shouldHave(text(testData.getProject().getName()));

    }

    @Test
    // Boundary values test: max characters limit for project ID (255 characters)
    // Actually there are no limitations on UI, project can be created with ANY name
    public void authorizedUserShouldBeAbleToCreateProjectWith255CharactersName() {
        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        String longNumber225 = RandomData.getString225();
        testData.getProject().setName(longNumber225);

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement projectWasCreated = element(Selectors.byId("unprocessed_objectsCreated"));
        String expectedText = String.format("New project \"%s\", build configuration \"%s\" and VCS root \"%s\" have been successfully created.", testData.getProject().getName(), testData.getBuildType().getName(), url + "#refs/heads/master");
        projectWasCreated.shouldHave(text(expectedText));

        new ProjectsPage().open().getSubprojects();

        SelenideElement currentProject = element(Selectors.byClass("Subproject__entity--nm"));
        currentProject.shouldHave(text(testData.getProject().getName()));

    }

    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR PROJECT CREATION ON UI USE CASE ************

    @Test
    public void authorizedUserShouldNotBeAbleToCreateProjectWithEmptyName() {
        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        testData.getProject().setName(" ");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement errorMessage = element(Selectors.byId("error_projectName"));
        errorMessage.shouldHave(text("Project name must not be empty"));
    }

    @Test
    public void authorizedUserShouldNotBeAbleToCreateProjectWithSameName() {
        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        testData.getBuildType().setName(RandomData.getString());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement errorMessage = element(Selectors.byId("error_projectName"));
        errorMessage.shouldHave(text("Project with this name already exists: " + testData.getProject().getName()));
    }
}


