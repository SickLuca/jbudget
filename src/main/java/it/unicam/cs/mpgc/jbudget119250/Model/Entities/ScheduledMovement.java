package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Controller.MovementType;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a scheduled financial movement in the system.
 * <p>
 * The {@code ScheduledMovement} class extends the {@code AbstractMovement} class,
 * inheriting core properties and behavior. This class adds a property for the scheduled
 * date of the movement.
 * <p>
 * It is mapped to the "SCHEDULED_MOVEMENTS" table in the database using JPA annotations.
 */
@Entity (name = "SCHEDULED_MOVEMENTS")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ScheduledMovement extends AbstractMovement {

    private LocalDateTime scheduleDate;

    @Override
    public MovementType getMovementType() {
        return MovementType.SCHEDULED_MOVEMENT;
    }
}
