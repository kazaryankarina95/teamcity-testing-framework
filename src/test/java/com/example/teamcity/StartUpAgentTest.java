package com.example.teamcity;

import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.models.Agent;
import com.example.teamcity.api.models.AgentList;
import com.example.teamcity.api.requests.AgentRequests;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.testng.annotations.Test;

public class StartUpAgentTest extends BaseApiTest {
    @Test
    @Tag("SetupTest")
    public void authorizeAgentTest() {
        var requestedAgent = new AgentRequests(Specifications.getSpec().superUserSpec());
        int retries = 10; // Max number of attempts
        for (int i = 0; i < retries; i++) {
            AgentList listOfAgents = requestedAgent.get();

            // If list is not empty, break the loop
            if (!listOfAgents.getAgent().isEmpty()) {
                break;
            }

            // If list is empty, wait for a while before next try
            try {
                Thread.sleep(10000); // Sleep for 500 milliseconds before next try
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If this is the last iteration and list is still empty, handle the error appropriately
            if (i == retries - 1) {
                // Handle error
            }

            System.out.println(listOfAgents);

            Agent defaultAgent = listOfAgents.getAgent().get(0);
            var defaultAgentName = defaultAgent.getName();
            System.out.println(defaultAgentName);

            requestedAgent.put(defaultAgentName)
                    .then().assertThat().statusCode(HttpStatus.SC_OK)
                    .extract().asString();
        }
    }
}