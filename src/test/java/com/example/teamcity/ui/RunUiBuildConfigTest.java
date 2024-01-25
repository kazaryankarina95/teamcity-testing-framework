package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.element;

public class RunUiBuildConfigTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleToRunBuildConfig() {
        var testData = TestDataStorage.addTestData();
        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByURL(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));

        SelenideElement projectWasCreated = element(Selectors.byClass("ring-icon-icon SvgIcon__icon--wZ Subproject__arrow--KJ CollapsibleLine__arrow--so"));
        projectWasCreated.click();

        SelenideElement buildWasCreated = element(Selectors.byClass("MiddleEllipsis__searchable--uZ"));
        buildWasCreated.shouldHave(text(testData.getProject().getName()));

    }
}
