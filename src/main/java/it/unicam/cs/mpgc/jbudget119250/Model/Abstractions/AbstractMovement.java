package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import it.unicam.cs.mpgc.jbudget119250.Controller.MovementType;

import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultUser;

import it.unicam.cs.mpgc.jbudget119250.Model.Entities.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



/**
 * Abstract base class for defining financial operations.
 * A movement is characterized by a monetary amount, an operation type (income or expense),
 * associated tags, a date, and a user.
 * <p>
 * The class implements the {@code Movement} interface and uses JPA annotations for persistence.
 * Subclasses of {@code AbstractMovement} must define the specific type of movement by implementing
 * the abstract method {@code getMovementType}.
 */
@Entity
@Inheritance( strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
public abstract class AbstractMovement implements Movement<DefaultTag, DefaultUser> {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private OperationType operationType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movement_tag",
            joinColumns = @JoinColumn(name = "movement_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<DefaultTag> tag = new ArrayList<>();

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DefaultUser user;

    public abstract MovementType getMovementType();

}
