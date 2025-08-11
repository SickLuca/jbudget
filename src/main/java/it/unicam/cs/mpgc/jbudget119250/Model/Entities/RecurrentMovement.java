package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Controller.MovementType;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a recurrent financial movement in the system.
 * <p>
 * The {@code RecurrentMovement} class extends the {@code AbstractMovement} class,
 * inheriting its attributes and behaviors, while adding specific properties for
 * handling recurrent movements such as frequency and deadline.
 * <p>
 * This class is mapped to the "RECURRENT_MOVEMENTS" table in the database using
 * JPA annotations.
 */

@Entity (name = "RECURRENT_MOVEMENTS")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RecurrentMovement extends AbstractMovement {

    private Integer frequency;

    private LocalDateTime deadLine;

    @Override
    public MovementType getMovementType() {
        return MovementType.RECURRENT_MOVEMENT;
    }
}
