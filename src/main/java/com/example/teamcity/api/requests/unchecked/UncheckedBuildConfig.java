package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildConfig extends Request implements CrudInterface {

    // how to create build config https://www.jetbrains.com/help/teamcity/rest/create-and-delete-build-configurations.html
    private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes";
    private static final String BUILD_RUN_ENDPOINT = "/app/rest/buildQueue";


    public UncheckedBuildConfig(RequestSpecification spec) {

        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(BUILD_CONFIG_ENDPOINT);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given()
                .spec(spec)
                .delete(BUILD_CONFIG_ENDPOINT + "/id:" + id);
    }

    public Response run(String id) {
        String PAYLOAD = """
                {
                	"buildType": {
                    	"id": "%s"
                	}
                }
                """;
        String formatted = PAYLOAD.formatted(id);
        return given()
                .spec(spec)
                .header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .body(formatted)
                .post(BUILD_RUN_ENDPOINT);
    }
}
