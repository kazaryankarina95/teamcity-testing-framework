package com.example.teamcity.api.requests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildRun extends Request {
    private static final String BUILD_RUN_ENDPOINT = "/app/rest/buildQueue";

    public UncheckedBuildRun(RequestSpecification spec) {
        super(spec);
    }

    public Response run(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .header("Content-Type", ContentType.JSON)
                .header("Accept", ContentType.JSON)
                .post(BUILD_RUN_ENDPOINT);
    }

}
