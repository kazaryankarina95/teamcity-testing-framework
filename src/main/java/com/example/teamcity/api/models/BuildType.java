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
@JsonIgnoreProperties({"projectName", "projectId", "href", "webUrl", "buildTypeId", "parentProjectId", "templates", "vcs-root-entries", "settings", "parameters", "internalId", "templateFlag", "type", "paused", "uuid", "description", "projectInternalId", "inherited", "links", "template", "steps", "features", "triggers", "snapshot-dependencies", "artifact-dependencies", "agent-requirements", "branches", "builds", "investigations", "compatibleAgents", "compatibleCloudImages", "vcsRootInstances", "externalStatusAllowed", "pauseComment", "locator"})
public class BuildType {
    // https://www.jetbrains.com/help/teamcity/rest/buildtype.html
    private String id;
    private NewProjectDescription project;
    private String name;
}
