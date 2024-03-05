package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    // https://www.jetbrains.com/help/teamcity/rest/agent.html
    private String id;
    private String name;
    private Integer typeId;
    private String href;
    private String webUrl;
}
