package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Locator;

public interface CrudInterface {
    Object create(Object obj);

    Object get(Locator locator, String entityByNameOrId);

    Object update(Object obj);

    Object delete(String id);
}
