package com.example.teamcity;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.BaseUiTest;
import com.example.teamcity.ui.pages.admin.StartUpPage;
import org.junit.jupiter.api.Tag;
import org.testng.annotations.Test;

public class SetupTest extends BaseUiTest {
    @Test
    @Tag("SetupTest")
    public void startUpServerTest() {
        new StartUpPage()
                .open()
                .setupTeamCityServer()
                .getHeader().shouldHave(Condition.text("Create Administrator account"));
    }
}