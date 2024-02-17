package com.example.teamcity.api;

import com.example.teamcity.api.enums.Locator;
import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class RolesTest extends BaseApiTest {
    // This test won`t work anymore since we changed username and password from admin/admin to random data generator
    @Test
    public void unauthorisedUser() {
        var testData = testDataStorage.addTestData();
        uncheckedWithSuperUser.getProjectRequest()
       // new UncheckedRequests(Specifications.getSpec().unauthSpec()).getProjectRequest()
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedWithSuperUser.getProjectRequest()
                // get method is incorrect now. Will need to figure out how to improve it
                .get(Locator.BY_ID, "id")
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + testData.getProject().getId() + "'"));
    }

    @Test
    public void systemAdminTest() {
        var testData = testDataStorage.addTestData();
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getUser());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminRightsPositiveTest() {
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        new CheckedUser(Specifications.getSpec().superUserSpec())
                .create(testData.getUser());

        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                        .create(testData.getProject());

        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }
    @Test
    public void projectAdminRightsNegativeTest() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        var firstUserRequest = new CheckedRequests(Specifications.getSpec().authSpec(firstTestData.getUser()));
        var secondUserRequest = new CheckedRequests(Specifications.getSpec().authSpec(secondTestData.getUser()));

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        // For 1st Admin
        firstTestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(firstTestData.getUser());

        // For 2nd Admin
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest()
                .create(secondTestData.getUser());

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

      //  softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }

}
