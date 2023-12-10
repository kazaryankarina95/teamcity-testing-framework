package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.config.Config;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public class BaseUiTest extends BaseTest {
    // in TestNG framework we use @BeforeSuite since tests are run by suits
    @BeforeSuite
    public void setupUiTests() {
        Configuration.browser = "firefox";
        Configuration.baseUrl = "http://" + Config.getProperties("host");
        Configuration.remote = Config.getProperties("remote");

        // We need to pass some Selenoid settings to Selenide for Selenoid to understand that UI also should be displayed (for us to see a browser).
        Configuration.reportsFolder = "target/surefire-reports";
        Configuration.downloadsFolder ="target/downloads";

        Map<String, Boolean> options = new HashMap<>();
        options.put("enableVNC", true);
        options.put("enableLog", true);

        FirefoxOptions capabilities = new FirefoxOptions();
        Configuration.browserCapabilities = capabilities;
        Configuration.browserCapabilities.setCapability("selenoid:options", options);

    }
}
