package com.example.teamcity.api.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Properties;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("properties")
public class User {
    // here we can find how to name fields correctly: https://www.jetbrains.com/help/teamcity/rest/user.html
    private String username;
    private String password;
    private String email;
    private Roles roles;
    private Long id;
    private String href;
    private String locator;
}
