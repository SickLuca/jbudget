package it.unicam.cs.mpgc.jbudget119250.Controller;


/**
 * Concrete implementation of the {@code AbstractJpaController} class
 * designed to provide basic CRUD operations for managing JPA entities.
 *
 * @param <T> the type of the entity managed by this controller
 */
public class DefaultJpaController<T> extends AbstractJpaController<T>{

    public DefaultJpaController(Class<T> entityClass) {
        super(entityClass);
    }
}
