package com.example.teamcity.api;

import com.codeborne.selenide.Configuration;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class    BaseTest {
    protected SoftAssertions softy;

    @BeforeMethod
    public void beforeTest() {
        softy = new SoftAssertions();
    }

    @AfterMethod
    public void afterTest() {
        softy.assertAll();
    }
}