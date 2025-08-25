package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractCategory;
import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the default implementation of the {@code Tag} interface for categorizing
 * entities within the system. This class is designed to associate entities with a specified
 * category, facilitating the organization and classification of domain objects.
 * <p>
 * The {@code DefaultTag} class implements the functionality defined in the {@code Tag} interface
 * and is mapped to the "TAG" table in the database using JPA annotations. Each tag has an
 * identifier, a name, and an association with a category of type {@code AbstractCategory}.
 */
@Entity(name = "TAG")
@Getter
@Setter
@NoArgsConstructor
public class DefaultTag implements Tag<AbstractCategory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "category_id")
    private AbstractCategory category;

    @Override
    public String toString() {
        return this.name;
    }



}
