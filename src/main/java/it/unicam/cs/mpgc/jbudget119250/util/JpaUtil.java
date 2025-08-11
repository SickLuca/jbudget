package it.unicam.cs.mpgc.jbudget119250.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing the lifecycle and instances of JPA's {@link EntityManagerFactory}
 * and {@link EntityManager}. Provides methods to create, retrieve, and close these objects.
 * This class ensures a singleton pattern for the {@link EntityManagerFactory} to manage all JPA-related
 * operations within the application.
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "jbudget-persistence-unit";
    private static EntityManagerFactory entityManagerFactory;



    /**
     * Provides a singleton instance of the {@link EntityManagerFactory}. If the factory is not initialized
     * or is closed, it will create a new instance using the predefined persistence unit name.
     *
     * @return an initialized and open {@link EntityManagerFactory} instance to manage JPA entities.
     * @throws RuntimeException if the EntityManagerFactory cannot be initialized.
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (Exception e) {
                throw new RuntimeException("Impossible to initialize JPA", e);
            }
        }
        return entityManagerFactory;
    }



    /**
     * Provides a new instance of {@link EntityManager} from the {@link EntityManagerFactory}.
     * This method ensures interaction with the persistence context for managing JPA entities.
     *
     * @return a newly created {@link EntityManager} instance.
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }



    /**
     * Closes the {@link EntityManagerFactory} if it is initialized and currently open.
     * This method ensures that the EntityManagerFactory resources are released properly
     * when it is no longer necessary.
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

}