package it.unicam.cs.mpgc.jbudget119250.Controller;

import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;

import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.RecurrentMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.ScheduledMovement;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Enum representing different types of financial movements in the system.
 * Each movement type is associated with a specific entity class and
 * functionality for persistence.
 * <p>
 *
 * Each movement type is configured with:
 * - A supplier to instantiate the respective entity.
 * - A consumer to manage the save operation for that entity type.
 */
public enum MovementType {
    DEFAULT_MOVEMENT(
            DefaultMovement::new,
            movement -> new DefaultJpaController<>(DefaultMovement.class).save((DefaultMovement) movement)
    ),
    SCHEDULED_MOVEMENT(
            ScheduledMovement::new,
            movement -> new DefaultJpaController<>(ScheduledMovement.class).save((ScheduledMovement) movement)
    ),
    RECURRENT_MOVEMENT(
            RecurrentMovement::new,
            movement -> new DefaultJpaController<>(RecurrentMovement.class).save((RecurrentMovement) movement)
    );

    private final Supplier<AbstractMovement> instanceCreator;

    private final Consumer<AbstractMovement> saver;

    MovementType(Supplier<AbstractMovement> instanceCreator,Consumer<AbstractMovement> saver) {
        this.instanceCreator = instanceCreator;
        this.saver = saver;
    }

    /**
     * Retrieves a new instance of {@code AbstractMovement} created by the associated instance creator.
     *
     * @return a new instance of {@code AbstractMovement} specific to the movement type
     */
    public AbstractMovement getInstance() {
        return this.instanceCreator.get();
    }

    /**
     * Persists the provided {@code AbstractMovement} instance using the appropriate saving logic
     * defined for the specific type of movement.
     *
     * @param movement the movement to be saved
     */
    public void save(AbstractMovement movement) {
        saver.accept(movement);
    }
}
