package models.resources;

import java.io.Serializable;

public class MiscResource extends Resource implements Serializable {
    private double cost; // Cost per unit of the material

    // Constructor
    public MiscResource(int id, String name, double cost) {
        super(id, name);
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }
}
