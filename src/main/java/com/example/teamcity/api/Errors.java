package com.example.teamcity.api;

public final class Errors {
    // this is utility class contains no state, that`s why we have to create private constructor. We can`t create an instance of this class.
    private Errors() {
    }

    // ************ IN THE SECTION BELOW YOU CAN ALL CONSTANTS USED IN PROJECT CREATION ************

    public static final String PROJECT_NAME_ALREADY_EXISTS = "Project with this name already exists";
    public static final String PROJECT_CANT_HAVE_SAME_ID = "Project ID \"%s\" is already used by another project";
    public static final String PROJECT_WITH_WRONG_ROOT = "No project found by name or internal/external id '%s'";
    public static final String PROJECT_NAME_CANT_BE_NULL = "Project name cannot be empty.";
    public static final String PROJECT_ID_CANT_BE_EMPTY = "Project ID must not be empty.";
    public static final String PROJECT_NAME_CANT_BE_EMPTY = "Given project name is empty.";
    public static final String PROJECT_ID_CANT_HAVE_ID_226_CHARACTERS = "Project ID \"%s\" is invalid: it is 226 characters long while the maximum length is 225.";

    // ************ IN THE SECTION BELOW YOU CAN ALL CONSTANTS USED IN BUILD CONFIGURATION ************

    public static final String BUILD_CONFIG_WITH_SAME_NAME_ALREADY_EXISTS = "Build configuration with name \"%s\" already exists in project: \"%s\"";
    public static final String BUILD_CONFIG_ID_IS_USED = "The build configuration / template ID \"%s\" is already used by another configuration or template";
    public static final String BUILD_CONFIG_NAME_SHOULD_BE_PROVIDED = "When creating a build type, non empty name should be provided.";
    public static final String BUILD_CONFIG_CANT_BE_CREATED_WITH_PROJECT_NULL = "Build type creation request should contain project node.";
    public static final String BUILD_CONFIG_CANT_HAVE_INVALID_ID = "Build configuration or template ID \"%s\" is invalid: starts with non-letter character ' '. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).";
    public static final String BUILD_CONFIG_CANT_HAVE_ID_226_CHARACTERS = "Build configuration or template ID \"%s\" is invalid: it is 226 characters long while the maximum length is 225.";

}
