package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.PageElement;
import com.example.teamcity.ui.elements.ProjectElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.element;

public abstract class Page {
    private SelenideElement submitButton = element(Selectors.byType("submit"));

    private SelenideElement savingWaitingMarker = element(Selectors.byId("saving"));

    private SelenideElement pageWaitingMarker = element(Selectors.byDataTest("ring-loader"));

    public void submit() {
        submitButton.click();
        waitUntilDataIsSaved();
    }

    public void waitUntilPageIsLoaded() {
        pageWaitingMarker.shouldNotBe(Condition.visible, Duration.ofMinutes(1));
    }

    public void waitUntilDataIsSaved() {
        // We use "should" because all "should" methods contains assertions, so we can be sure that our test fails in case the output doesn`t meet expected result
        savingWaitingMarker.shouldNotBe(Condition.visible, Duration.ofSeconds(30));

    }

    // generic T (=type) is used to allow to use element field not only for ProjectElement but for PageElement
    public <T extends PageElement> List<T> generatePageElements(
            ElementsCollection collection,
            //creating list where we want to collect and store project elements
            Function<SelenideElement, T> creator) {
        var elements = new ArrayList<T>();

        collection.forEach(webElement -> {
            var pageElement = new ProjectElement(webElement);
            elements.add(creator.apply(webElement));
        });
        return elements;
    }
}
