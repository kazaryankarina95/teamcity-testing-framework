package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.element;
import static org.assertj.core.error.ShouldHave.shouldHave;
import static org.testng.AssertJUnit.assertEquals;

public class CreateNewBuildConfigTest extends BaseUiTest {

    // ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR BUILD CONFIGURATION ON UI USE CASE ************

    @Test
    // In this test we create a new project > go to favorite projects > find it by name, and make sure it exists
    public void authorizedUserShouldBeAbleToCreateBuildConfig() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

    }

    @Test
    public void authorizedUserShouldBeAbleToCreateBuildConfigWithLongBuildIdContainingDifferentCharacters() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        String longNumber = RandomData.generateStringWithSpecialCharacters(280);
        testData.getBuildType().setName(longNumber);

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());


        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

    }

    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR BUILD CONFIGURATION ON UI USE CASE ************

    @Test
    // Create a project with Build config with empty name
    public void authorizedUserShouldNotBeAbleToCreateBuildConfigWithEmptyId() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/karina.git";

        loginAsUser(testData.getUser());

        testData.getBuildType().setName(" ");

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        SelenideElement errorMessage = element(Selectors.byId("error_buildTypeName"));
        errorMessage.shouldHave(Condition.text("Build configuration name must not be empty"));

    }

}
