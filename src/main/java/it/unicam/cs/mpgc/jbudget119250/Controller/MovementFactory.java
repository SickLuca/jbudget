package it.unicam.cs.mpgc.jbudget119250.Controller;

import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractMovement;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultTag;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.DefaultUser;
import it.unicam.cs.mpgc.jbudget119250.Model.Entities.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory class responsible for creating instances of financial movements.
 * The class provides a builder pattern for dynamically constructing and validating
 * financial movement objects with specific attributes.
 */
public class MovementFactory {

    /**
     * Creates a {@code MovementBuilder} instance for the specified {@code MovementType}.
     *
     * @param type the type of movement for which the builder is created
     * @return a {@code MovementBuilder} instance initialized with the specified {@code MovementType}
     * @throws IllegalArgumentException if the provided {@code MovementType} is {@code null}
     */
    public MovementBuilder builderFor (MovementType type) {
        return new MovementBuilder(type);
    }


    /**
     * Builder class for creating instances of {@code AbstractMovement}.
     * This class enforces constraints and provides a fluent API for setting properties
     * required to build valid instances of movements.
     */
    public static class MovementBuilder {
        private final MovementType type;
        private OperationType operation;
        private Long id;
        private BigDecimal amount;
        private LocalDateTime date;
        private List<DefaultTag> tag;
        private DefaultUser user;

        private MovementBuilder(MovementType type) {
            if (type == null) {
                throw new IllegalArgumentException("");
            }
            this.type = type;
        }

        public MovementBuilder withId(Long id) {
            if (id == null) {
                throw new IllegalArgumentException("");
            }
            this.id = id;
            return this;
        }

        public MovementBuilder withAmount(BigDecimal amount) {
            if (amount == null) {
                throw new IllegalArgumentException("");
            }
            this.amount = amount;
            return this;
        }

        public MovementBuilder withDate(LocalDateTime date) {
            if (date == null) {
                throw new IllegalArgumentException("");
            }
            this.date = date;
            return this;
        }

        public MovementBuilder withTag(List<DefaultTag> tag) {
            if (tag == null) {
                throw new IllegalArgumentException("");
            }
            this.tag = tag;
            return this;
        }

        public MovementBuilder withUser(DefaultUser user) {
            if (user == null) {
                throw new IllegalArgumentException("");
            }
            this.user = user;
            return this;
        }

        public MovementBuilder withOperation(OperationType operation) {
            if (operation == null) {
                throw new IllegalArgumentException("");
            }
            this.operation = operation;
            return this;
        }

        /**
         * Builds and returns an instance of {@code AbstractMovement} using the properties
         * previously set in the builder. This method ensures that all mandatory fields are
         * properly initialized, and it performs validation before creating the movement.
         *
         * @return a new instance of {@code AbstractMovement} with all properties set
         *         according to the builder's state
         * @throws MovementCreationException if there is a failure during the creation
         *                                   of the movement instance
         */
        public AbstractMovement build() {
            validate();
            try {
                AbstractMovement movement = type.getInstance();
                movement.setAmount(amount);
                movement.setDate(date);
                movement.setTag(tag);
                movement.setUser(user);
                movement.setOperationType(operation);
                return movement;
            } catch (Exception e) {
                throw new MovementCreationException("Failed to create movement", e);
            }
        }

        private void validate() {
            if (amount == null) throw new IllegalStateException("Amount is required");
            if (date == null) throw new IllegalStateException("Date is required");
            if (tag == null) throw new IllegalStateException("Tag is required");
        }


    }
}
