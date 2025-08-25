package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for modeling hierarchical category structures. A category is
 * characterized by: ame, parent, and a list of children.
 * <p>
 * It provides properties and methods for managing parent-child relationships,
 * determining root categories, retrieving hierarchical paths, and handling child categories.
 * The class implements the {@code Category} interface and uses JPA annotations for persistence.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class AbstractCategory implements Category<AbstractCategory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AbstractCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<AbstractCategory> children = new ArrayList<>();

    /**
     * Retrieves the full hierarchical path of the category as a string.
     * The path represents the category and its parent categories in the hierarchy,
     * separated by ">".
     *
     * @return the full path of the category in the hierarchy
     */
    public String getFullPath() {
        if (this.parent == null) {
            return name;
        }
        return this.parent.getFullPath() + " > " + name;
    }

    @Override
    public String toString() {
        return "id = " + id + ", path = " + getFullPath();
    }
}
