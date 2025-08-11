package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

/**
 * Represents a generic category interface supporting hierarchical structures.
 * This interface defines methods for managing parent-child category relationships,
 * checking root status, retrieving a category's hierarchical path,
 * and accessing children by their unique identifiers.
 *
 * @param <T> the type of category implementing this interface.
 */

public interface Category<T extends Category<T>>{
    Long getId();

    /**
     * Add a category to the children list of the specified category.
     * Child category will be a subelement of the parent category in the hierarchical structure.
     *
     *
     * @param child the category to add as child of the specified parent
     */

    void addChild(T child);

    /**
     * Removes a child category from the children list of the current category based on the given child ID.
     *
     * @param id the unique identifier of the child category to be removed
     */

    void removeChild(Long id);

    /**
     * Determines if the current category instance is a root category.
     * A category is considered a root category if it has no parent category.
     *
     * @return true if the category has no parent and is thus a root category, otherwise false.
     */

    boolean isRoot();


    /**
     * Retrieves a child category from the children list of the current category based on the provided child ID.
     *
     * @param id the unique identifier of the child category to retrieve
     * @return the child category matching the given ID, or null if no child with the given ID is found
     * @throws IllegalArgumentException if the provided ID is null
     */
    T getChildById(Long id);


    /**
     * Retrieves the full hierarchical path of the category as a string.
     * The path represents the category and its parent categories in the hierarchy,
     * separated by ">".
     *
     * @return the full path of the category in the hierarchy
     */
    String getFullPath();

}
