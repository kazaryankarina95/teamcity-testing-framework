package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.config.Config;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class StartUpPage extends Page {

    @Getter
    private SelenideElement header = element(Selectors.byId("header"));
    private SelenideElement proceedButton = element("input[id='proceedButton']");
    private SelenideElement restoreButton = element("input[id='restoreButton']");
    private SelenideElement acceptLicense = element("input[id='accept']");
    private SelenideElement backupFileUploaded = element("input[id='backup']");

    public StartUpPage open() {
        Selenide.open("http://" + Config.getProperties("host"));
        return this;
    }

    public StartUpPage setupTeamCityServer() {
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        proceedButton.click();
        waitUntilPageIsLoaded();
        acceptLicense.shouldBe(Condition.enabled, Duration.ofMinutes(3));
        acceptLicense.scrollTo();
        acceptLicense.click();
        submit();
        return this;
    }
}

