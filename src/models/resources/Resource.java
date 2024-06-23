package models.resources;

import java.io.Serializable;

public abstract class Resource implements Serializable{
    protected String id; // Unique identifier for the resource
    protected String name; // Name of the resource

    // Constructor
    public Resource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters for id and name
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract double getCost(int nb); // Abstract method to get the cost of the resource
}

