package models.resources;

import java.io.Serializable;

// Material models.resources.Resource class
public class MaterialResource extends Resource implements Serializable {
    private double unitCost; // Cost per unit of the material
    private int quantity; // Quantity of the material

    // Constructor
    public MaterialResource(int id, String name, double unitCost) {
        super(id, name);
        this.unitCost = unitCost;
    }

    // Getter for unitCost
    public double getUnitCost() {
        return unitCost;
    }

    @Override
    public double getCost() {
        return unitCost * quantity;
    }
}
