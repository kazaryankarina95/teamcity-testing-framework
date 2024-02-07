package com.example.teamcity.api.requests;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.models.AgentList;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AgentRequests extends Request implements CrudInterface {

    private static final String AGENT_ENDPOINT = "/app/rest/agents";

    public AgentRequests(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    // getting agents via API: https://www.jetbrains.com/help/teamcity/rest/manage-agents.html#Get+Agents
    public Object get(Locator locator, String entityByNameOrId) {
        return given()
                .accept(ContentType.JSON)
                .get(AGENT_ENDPOINT + "/" + locator.getByNameOrId() + ":" + entityByNameOrId);
    }

    public AgentList get() {
        return given()
                .spec(spec)
                .accept(ContentType.JSON)
                .get(AGENT_ENDPOINT + "?locator=authorized:any")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(AgentList.class);
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Object delete(String id) {
        return null;
    }

    public Response put(String name) {
        String payload = """
                {
                  "comment" : {
                    "text" : "text"
                  },
                  "status" : true
                }
                """;
        System.out.println(payload);
        String s = "http://:" + Config.getProperties("superUserToken") + "@" + Config.getProperties("host") + AGENT_ENDPOINT + "/" + name + "/authorizedInfo";
        System.out.println(s);

        return given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .put(s);
    }
}
