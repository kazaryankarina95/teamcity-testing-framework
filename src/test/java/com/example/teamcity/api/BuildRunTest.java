package com.example.teamcity.api;

import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildRunTest extends BaseApiTest {

    // ************ IN THE SECTION BELOW YOU CAN FIND POSITIVE TEST CASES FOR PROJECT CREATION USE CASE ************

    @Test
    public void successfulBuildRunTest() {

        // We generate data for system admin/project/build config and add it into test data storage
        var testData = testDataStorage.addTestData();

        // As a system admin we create a new valid user
        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        // We create a new project with all required fields
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        // We create a new build config with all required fields
        var buildType = new CheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        // we run build config by ID that we just created by passing payload with build type ID
        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser())).run(buildType.getId());

        softy.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());

    }

    // ************ IN THE SECTION BELOW YOU CAN FIND NEGATIVE TEST CASES FOR PROJECT CREATION USE CASE ************

}
