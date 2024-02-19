package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private String name;
    private String value;
    private Boolean inherited;
    private Type type;
}
