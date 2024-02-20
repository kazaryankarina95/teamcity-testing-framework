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
@JsonIgnoreProperties({"projectName", "projectId", "href", "parentProjectId"})
// this is what we pass as an input when creating a new project
public class NewProjectDescription {

    // Get project`s fields from https://www.jetbrains.com/help/teamcity/rest/project.html
    private Project parentProject;
    private String name;
    private String id;
    private Boolean copyAllAssociatedSettings;
}
