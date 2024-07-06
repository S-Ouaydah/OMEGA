package models.resources;

import java.io.Serializable;

import static java.lang.Double.parseDouble;

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

    @Override
    public double getDuration() {
        return 0;
    }

    @Override
    public Object getVal(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> getId();
            case 1 -> getName();
            case 2 -> getCost();
            default -> null;
        };
    }
    @Override
    public void setVal(int columnIndex, Object aValue) {
        switch (columnIndex) {
            case 1 -> name = ((String) aValue);
            case 2 -> cost = parseDouble((String) aValue);
        }
    }
}
