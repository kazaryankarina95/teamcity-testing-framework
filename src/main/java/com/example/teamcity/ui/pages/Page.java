package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class Page {
    private SelenideElement submitButton = element(Selectors.byType("submit"));

    private SelenideElement savingWaitingMarker = element(Selectors.byId("saving"));

    public void submit() {
        submitButton.click();
        waitUntilDataIsSaved();
    }

    public void waitUntilDataIsSaved() {
        // We use "should" because all "should" methods contains assertions, so we can be sure that our test fails in case the output doesn`t meet expected result
        savingWaitingMarker.shouldBe(Condition.not(Condition.visible), Duration.ofSeconds(30));


    }
}
