package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"internalId", "uuid", "parentProjectInternalId", "parentProjectName", "archived", "virtual", "description", "href", "webUrl", "links", "parentProject", "readOnlyUI", "defaultTemplate", "buildTypes", "templates", "parameters", "vcsRoots", "projectFeatures", "projects", "cloudProfiles", "ancestorProjects", "deploymentDashboards"})
// this is what we get as an output when creating a new project
public class Project {
    // Get project`s field from https://www.jetbrains.com/help/teamcity/rest/project.html
    private String id;
    private String name;
    private String parentProjectId;
    private String locator;
}
