package it.unicam.cs.mpgc.jbudget119250.Controller;

import java.util.List;

/**
 * A generic interface for a Controller that provides standard CRUD operations.
 *
 * @param <T> the type of the entity managed by the controller
 */
public interface Controller<T> {

    /**
     * Persists the provided entity into the underlying data storage.
     *
     * @param t the entity to be saved
     */
    void save(T t);


    /**
     * Deletes an entity identified by its unique identifier from the underlying data storage.
     *
     * @param id the unique identifier of the entity to be deleted
     */
    void delete(Long id);

    /**
     * Updates the provided entity in the underlying data storage. The entity should already exist,
     * and this method applies changes to the existing record in the data storage.
     *
     * @param t the entity containing updated fields to be persisted in the data storage
     */
    void update(T t);

    /**
     * Retrieves an entity identified by its unique identifier from the underlying data storage.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return the entity associated with the specified identifier, or null if no entity is found
     */
    T getById(Long id);

    /**
     * Retrieves all entities of type T from the underlying data storage.
     *
     * @return a list of all entities managed by the controller
     */
    List<T> getAll();
}

