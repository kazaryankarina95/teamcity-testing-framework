package com.example.teamcity.api.models;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    // here we can find how to name fields correctly: https://www.jetbrains.com/help/teamcity/rest/user.html
    private String username;
    private String password;
    private String email;
    private Roles roles;
    private Long id;
    private String href;
}
