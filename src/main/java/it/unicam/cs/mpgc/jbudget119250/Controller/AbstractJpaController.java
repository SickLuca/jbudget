package it.unicam.cs.mpgc.jbudget119250.Controller;

import it.unicam.cs.mpgc.jbudget119250.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Abstract class implementing the {@link Controller} interface,
 * providing a base implementation for managing JPA entities with CRUD operations.
 *
 * @param <T> the type of entity managed by this controller
 */
public abstract class AbstractJpaController<T> implements Controller<T>{

    private final EntityManagerFactory emf;
    private final Class<T> entityClass;

    public AbstractJpaController (Class<T> entityClass) {
        this.emf = JpaUtil.getEntityManagerFactory();
        this.entityClass = entityClass;
    }

    protected void executeInTransaction(Consumer<EntityManager> operation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;
        try (em) {
            transaction = em.getTransaction();
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
            throw new RuntimeException("Error during the operation: " + e.getMessage(), e);
        }
    }

    protected <R> R executeQuery(Function<EntityManager, R> query) {
        try (EntityManager em = emf.createEntityManager()) {
            return query.apply(em);
        }
    }

    private void rollbackTransaction(EntityTransaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception e) {
                throw new RuntimeException("Errore durante il rollback della transazione: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void save(T movement) {
        executeInTransaction(em -> em.persist(movement));
    }

    @Override
    public void delete(Long id) {
        executeInTransaction(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    @Override
    public void update(T t) {
        executeInTransaction(em -> em.merge(t));
    }

    @Override
    public T getById(Long id)  {
        return executeQuery(em -> em.find(entityClass, id));
    }

    @Override
    public List<T> getAll() {
        return executeQuery(em -> em.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass).getResultList());
    }
}
