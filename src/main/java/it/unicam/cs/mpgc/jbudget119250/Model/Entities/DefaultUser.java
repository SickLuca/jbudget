package it.unicam.cs.mpgc.jbudget119250.Model.Entities;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.Profile;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the default implementation of the {@code Profile} interface.
 * This class is primarily used to model a user, containing their basic
 * personal information such as a unique identifier, name, and surname.
 * <p>
 * The {@code DefaultUser} class is mapped to the "PROFILE" table in the database
 * using JPA annotations.
 */
@Entity(name = "PROFILE")
@Getter
@Setter
public class DefaultUser implements Profile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Override
    public String toString() {
        return  id + ": " + name + " " + surname + " ";
    }
}
