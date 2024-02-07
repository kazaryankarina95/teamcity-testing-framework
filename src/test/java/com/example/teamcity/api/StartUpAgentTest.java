package com.example.teamcity.api;

import com.example.teamcity.api.models.Agent;
import com.example.teamcity.api.models.AgentList;
import com.example.teamcity.api.requests.AgentRequests;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.testng.annotations.Test;

public class StartUpAgentTest extends BaseApiTest {
    @Test
    @Tag("Regression")
    public void authorizeAgentTest() {
        var requestedAgent = new AgentRequests(Specifications.getSpec().superUserSpec());
        AgentList listOfAgents = requestedAgent.get();
        System.out.println(listOfAgents);

        Agent defaultAgent = listOfAgents.getAgent().get(0);
        var defaultAgentName = defaultAgent.getName();
        System.out.println(defaultAgentName);

        requestedAgent.put(defaultAgentName)
        .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}