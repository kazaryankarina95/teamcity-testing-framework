package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedUser extends Request implements CrudInterface {
    // TeamCity doc to create user: https://www.jetbrains.com/help/teamcity/rest/manage-users.html#Create+User
    private final static String USER_ENDPOINT = "/app/rest/users";

    public UncheckedUser(RequestSpecification spec) {
        super(spec);
    }

    @Override
    // Create User: https://www.jetbrains.com/help/teamcity/rest/manage-users.html#Create+Usera
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(USER_ENDPOINT);
    }

    @Override
    public Response get(Locator locator, String entityByNameOrId) {
        return null;
    }

    @Override
    public Response update(Object obj) {
        return null;
    }

    @Override
    // Delete User: https://www.jetbrains.com/help/teamcity/rest/manage-users.html#Update%2FRemove+Specific+User
    public Response delete(String id) {
        return given()
                .spec(spec)
                .delete(USER_ENDPOINT + "/username" + id);
    }
}
