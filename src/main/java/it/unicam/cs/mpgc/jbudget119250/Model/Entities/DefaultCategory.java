package it.unicam.cs.mpgc.jbudget119250.Model.Entities;

import it.unicam.cs.mpgc.jbudget119250.Model.Abstractions.AbstractCategory;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Represents the default implementation of a category within the system.
 * This class extends the {@code AbstractCategory} class, inheriting its
 * properties and methods for managing hierarchical category structures.
 * <p>
 * The {@code DefaultCategory} class is annotated as a JPA entity and is mapped
 * to the "DEFAULT_CATEGORY" table in the database. This implementation provides
 * persistence and object-relational mapping capabilities for default categories
 * within the domain model.
 */

@Entity(name = "DEFAULT_CATEGORY")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DefaultCategory extends AbstractCategory {

}
