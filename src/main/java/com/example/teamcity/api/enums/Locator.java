package com.example.teamcity.api.enums;

import lombok.Getter;

@Getter
public enum Locator {
    BY_NAME("name"),
    BY_ID("id");

    private final String byNameOrId;

    Locator(String byNameOrId) {
        this.byNameOrId = byNameOrId;
    }

}
