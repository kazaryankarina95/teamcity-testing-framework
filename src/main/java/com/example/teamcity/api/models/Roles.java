package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    // Roles include the following fields: https://www.jetbrains.com/help/teamcity/rest/roles.html
    private List<Role> role;
}
