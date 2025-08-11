package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for modeling hierarchical category structures. A category is
 * characterized by a name, a parent and a list of children.
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
    @JoinColumn(name = "parent_id") //category_id?
    private AbstractCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AbstractCategory> children = new ArrayList<>();

    public boolean isRoot() {
        return this.parent == null;
    }

    @Override
    public void addChild(AbstractCategory child) {
        if (child != null) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    @Override
    public void removeChild(Long id) {
        if (id != null) {
            this.getChildById(id).setParent(null);
            this.getChildren().removeIf(child -> child.getId().equals(id));
        }
    }

    @Override
    public AbstractCategory getChildById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return this.getChildren().stream()
                .filter(child -> child.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

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
