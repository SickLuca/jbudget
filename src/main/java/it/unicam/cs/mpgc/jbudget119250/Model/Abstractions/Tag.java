package it.unicam.cs.mpgc.jbudget119250.Model.Abstractions;

public interface Tag<T extends Category<T>> {

    Long getId();

    String getName();

    void setName(String name);

    T getCategory();

    void setCategory(T category);

}
