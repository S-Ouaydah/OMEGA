package models.resources;

import java.io.Serializable;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

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

    @Override
    public double getDuration() {
        return 0;
    }

    @Override
    public Object getVal(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> getId();
            case 1 -> getName();
            case 2 -> getUnitCost();
            case 3 -> quantity;
            default -> null;
        };
    }
    @Override
    public void setVal(int columnIndex, Object aValue) {
        switch (columnIndex) {
            case 1 -> name = ((String) aValue);
            case 2 -> unitCost = parseDouble((String) aValue);
            case 3 -> quantity = parseInt((String) aValue);
        }
    }
}
