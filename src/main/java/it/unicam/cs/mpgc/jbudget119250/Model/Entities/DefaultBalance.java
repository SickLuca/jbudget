package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.Balance;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents the default implementation of the {@code Balance} interface.
 * This entity defines the balance information, primarily consisting of an
 * identifier and a monetary balance. It is mapped to the "BALANCE" table in
 * a database using JPA annotations.
 * <p>
 * The {@code DefaultBalance} class provides persistence and object-relational
 * mapping features while implementing the core functionality defined in the
 * {@code Balance} interface.
 */
@Entity (name = "BALANCE")
@Data
@ToString
@Setter
public class DefaultBalance implements Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

}
