package models.resources;

import java.io.Serializable;

public abstract class Resource implements Serializable{
    protected int id; // Unique identifier for the resource
    protected String name; // Name of the resource

    // Constructor
    public Resource(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters for id and name
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract double getCost(); // Abstract method to get the cost of the resource

    public String setName(String name) {
        return this.name = name;
    }
}

