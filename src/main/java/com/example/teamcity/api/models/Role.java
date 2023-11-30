package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    // Role includes the following fields: https://www.jetbrains.com/help/teamcity/rest/role.html
    private String roleId;
    private String scope;
    private String href;

}
