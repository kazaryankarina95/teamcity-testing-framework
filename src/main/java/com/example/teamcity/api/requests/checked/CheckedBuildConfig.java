package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class CheckedBuildConfig extends Request implements CrudInterface {
    public CheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public BuildType create(Object obj) {
        return new UncheckedBuildConfig(spec).create(obj)
                        .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class);
    }

    @Override
    public Object get(Locator locator, String entityByNameOrId) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public String delete(String id) {
        return new UncheckedBuildConfig(spec).delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

    public String run(String id) {
        return new UncheckedBuildConfig(spec).run(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();
    }
}
