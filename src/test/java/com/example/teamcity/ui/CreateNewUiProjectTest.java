package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.element;

// ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION ON UI USE CASE ************

public class CreateNewUiProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateProject() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

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
        errorMessage.shouldHave(Condition.text("Project name must not be empty"));
    }
}


