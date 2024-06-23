// Material Resource class
public class MaterialResource extends Resource {
    private double unitCost; // Cost per unit of the material

    // Constructor
    public MaterialResource(String id, String name, double unitCost) {
        super(id, name);
        this.unitCost = unitCost;
    }

    // Getter for unitCost
    public double getUnitCost() {
        return unitCost;
    }

    @Override
    public double getCost(int quantity) {
        // Calculate cost based on unit cost
        return unitCost * quantity;
    }
}
