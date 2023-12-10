package com.example.teamcity.ui;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import org.testng.annotations.Test;

public class CreateNewUiProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToCreateProject() {

        var testData = TestDataStorage.addTestData();

        var url = "https://github.com/karinakazaryan/TestProject";

        loginAsUser(testData.getUser());

        new CreateNewProject().open(testData.getProject().getParentProject().getId())
                .createProjectByURL(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
    }
}
