package com.example.teamcity.ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public abstract class PageElement {

    private final SelenideElement element;

    public PageElement(SelenideElement element) {
        //here we store 1 container with header, icon, etc
        this.element = element;

    }

    public SelenideElement findElement(By by) {
        return element.find(by);
    }

    public SelenideElement findElement(String value) {
        return element.find(value);
    }

    public ElementsCollection findElements(By by) {
        // findAll if we want to find collection of elements
        return element.findAll(by);
    }
}
