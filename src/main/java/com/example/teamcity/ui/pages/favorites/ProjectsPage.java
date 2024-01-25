package com.example.teamcity.ui.pages.favorites;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.ProjectElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.elements;

public class ProjectsPage extends FavoritesPage {
    public static final String FAVORITE_PROJECTS_URL = "/favorite/projects";

    // we have got a list of elements from Selenide collection
    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--WE"));

    // but we want to get List<ProjectElement>. So we need to get data from <ElementCollection> to List<ProjectElement>
    public ProjectsPage open() {
        Selenide.open(FAVORITE_PROJECTS_URL);
        waitUntilFavoritePageIsLoaded();
        // we return the same page, that`s why return this
        return this;
    }

    public List<ProjectElement> getSubprojects() {
        return generatePageElements(subprojects, ProjectElement::new);
        // ProjectElement::new - we invoked our constructor, and used it while creating a new element in Page class (where we used <T>
    }




}
