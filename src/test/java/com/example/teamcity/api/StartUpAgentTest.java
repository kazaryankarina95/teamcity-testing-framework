package com.example.teamcity.api;

import com.codeborne.selenide.Configuration;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.Agent;
import com.example.teamcity.api.models.AgentList;
import com.example.teamcity.api.requests.AgentRequests;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StartUpAgentTest extends BaseApiTest {

/*    @BeforeClass
    public static void setUp() {
        Configuration.remote = Config.getProperties("remote");
        Configuration.browser = Config.getProperties("browser");
    } */

    @Test
    public void authorizeAgentTest() {
        var requestedAgent = new AgentRequests(Specifications.getSpec().superUserSpec());
        AgentList listOfAgents = requestedAgent.get();
        System.out.println(listOfAgents);

        Agent defaultAgent = listOfAgents.getAgent().get(0);
        var defaultAgentName = defaultAgent.getName();
        System.out.println(defaultAgentName);

        var authorizedAgent = requestedAgent.put(defaultAgentName)
        .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();

       // System.out.println("Response body: " + authorizedAgent.getBody().asString());
    }
}