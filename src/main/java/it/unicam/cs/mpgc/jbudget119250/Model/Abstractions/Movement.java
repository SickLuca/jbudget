package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import it.unicam.cs.mpgc.jbudget119250.Controller.MovementType;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a generic movement in a financial system. A movement is an operation
 * such as income or expense, associated with a user, amount, tags, and an operation type.
 * This interface provides methods to access movement details, including its ID,
 * amount, associated tags, date, operation type, user, and movement type.
 *
 * @param <T> the type of tag associated with the movement
 * @param <U> the type of user associated with the movement
 */
public interface Movement<T, U> {
    Long getId();

    BigDecimal getAmount();

    List<T> getTag();

    LocalDateTime getDate();

    OperationType getOperationType();

    U getUser();

    MovementType getMovementType();

    void setAmount(BigDecimal amount);

    void setTag(List<T> tag);

    void setDate(LocalDateTime date);

    void setOperationType(OperationType operationType);

    void setUser(U user);

}
