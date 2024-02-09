package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.element;

// The same as page object where we describe web element but for a specific element
@Getter
public class ProjectElement extends PageElement {

    private final SelenideElement header;
    private final SelenideElement icon;

    public ProjectElement(SelenideElement element) {
        super(element);
        this.header = element(Selectors.byClass("Subprojects__limitWidth--Xw"));
        this.icon = findElement("svg");

    }
}
