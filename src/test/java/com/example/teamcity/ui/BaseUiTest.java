package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.LoginPage;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public class BaseUiTest extends BaseTest {
    // in TestNG framework we use @BeforeSuite since tests are run by suits
    @BeforeSuite
    public void setupUiTests() {

        Configuration.baseUrl = "http://" + Config.getProperties("host");
        Configuration.remote = Config.getProperties("remote");

        // We need to pass some Selenoid settings to Selenide for Selenoid to understand that UI also should be displayed (for us to see a browser).
        Configuration.reportsFolder = "target/surefire-reports";
        Configuration.downloadsFolder = "target/downloads";

        BrowserSettings.setup(Config.getProperties("browser"));

    }

    public void loginAsUser(User user) {
        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(user);
        new LoginPage().open().login(user);
    }
}
