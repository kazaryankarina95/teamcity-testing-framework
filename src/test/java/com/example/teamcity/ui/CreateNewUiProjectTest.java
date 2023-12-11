package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

public class CreateNewUiProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateProject() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/TestProject";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage()
                .open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

        }
    }
}
