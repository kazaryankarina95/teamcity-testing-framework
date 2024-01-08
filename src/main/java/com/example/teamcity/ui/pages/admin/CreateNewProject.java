package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import static com.codeborne.selenide.Selenide.element;

public class CreateNewProject extends Page {

    private SelenideElement urlInput = null;

    private SelenideElement projectNameInput = null;

    private SelenideElement projectCreation = null;

    private SelenideElement buildTypeNameInput = null;

    public CreateNewProject open(String parentProjectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        urlInput = element(Selectors.byId("url"));
        return this;
    }

    public CreateNewProject createProjectByURL(String url) {
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    public CreateNewProject setupProject(String projectName, String buildTypeName) {
        projectNameInput = element(Selectors.byId("projectName"));
        buildTypeNameInput = element(Selectors.byId("buildTypeName"));
        projectCreation = element(Selectors.byName("createProject"));
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        projectCreation.click();
        waitUntilPageIsLoaded();
        return this;
    }
}
