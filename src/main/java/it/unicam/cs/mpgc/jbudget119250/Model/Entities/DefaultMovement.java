package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Controller.MovementType;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * Represents the default implementation of a financial movement in the system.
 * The {@code DefaultMovement} class extends the {@code AbstractMovement} class,
 * inheriting its properties and behavior such as amount, operation type, tags,
 * date, and user association.
 * <p>
 * This class serves as the concrete representation of a movement with a
 * {@code DEFAULT_MOVEMENT} type and is mapped to the "DEFAULT_MOVEMENT" table
 * in the database using JPA annotations.
 */
@Entity(name = "DEFAULT_MOVEMENT")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DefaultMovement extends AbstractMovement {

    @Override
    public MovementType getMovementType() {
        return MovementType.DEFAULT_MOVEMENT;
    }
}
