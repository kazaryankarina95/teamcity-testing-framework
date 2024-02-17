package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

// Option+Enter = Implement methods
public class UncheckedProject extends Request implements CrudInterface {

    private static final String PROJECT_ENDPOINT = "/app/rest/projects";

    public UncheckedProject(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(PROJECT_ENDPOINT);
    }
// teamcity spec https://www.jetbrains.com/help/teamcity/rest/get-project-details.html
    @Override
    public Response get(Locator locator, String entityByNameOrId) {
        return given()
                .spec(spec)
                .get(PROJECT_ENDPOINT + "/" + locator.getByNameOrId() + ":" + entityByNameOrId);
    }

    @Override
    public Response update(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .put(PROJECT_ENDPOINT + "/id:" + obj);
    }

    // https://www.jetbrains.com/help/teamcity/rest/create-and-delete-projects.html#Delete+Project
    @Override
    public Response delete(String id) {
        return given()
                .spec(spec)
                .delete(PROJECT_ENDPOINT + "/id:" + id);
    }
}
