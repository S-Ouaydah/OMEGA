package omega.models.resources;

import java.io.Serializable;

public abstract class Resource implements Serializable{
    protected int id; // Unique identifier for the resource
    protected String name; // Name of the resource
    public enum resourceTypes {
            Material(new String[]{"ID", "Name", "Unit Cost", "Quantity"}),
            Human(new String[]{"ID", "Name", "Role", "Hourly Rate", "Hours"}),
            Misc(new String[]{"ID", "Name", "Cost"});
            public final String[] COLUMN_NAMES;
            resourceTypes(String[] cols) {
                this.COLUMN_NAMES = cols;
            }
            public String[] getColumnNames() {
                return COLUMN_NAMES;
            }
    };

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
    public abstract Object getVal(int columnIndex);
    public abstract void setVal(int column, Object aValue);
}

