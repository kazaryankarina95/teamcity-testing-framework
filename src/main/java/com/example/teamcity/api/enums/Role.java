package com.example.teamcity.api.enums;


// TeamCity roles: https://www.jetbrains.com/help/teamcity/managing-roles-and-permissions.html#Per-Project+Authorization+Mode
// There are described roles in roles-config.xml stored in Workshop/teamcity-server/datadir/config
public enum Role {
    SYSTEM_ADMIN("SYSTEM_ADMIN"),

    PROJECT_VIEWER("PROJECT_VIEWER"),

    PROJECT_ADMIN("PROJECT_ADMIN"),

    PROJECT_DEVELOPER("PROJECT_DEVELOPER"),

    AGENT_MANAGER("AGENT_MANAGER");

    private String text;

    Role(String text) {
        this.text = text;

    }

    public String getText() {

        return text;
    }
}
