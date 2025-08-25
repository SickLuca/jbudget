package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import java.util.List;

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

    T getParent();

    void setParent(T parent);

    List<T> getChildren();

}
