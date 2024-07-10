package omega.models.resources;

import java.io.Serializable;

public abstract class Resource implements Serializable{
    protected String uid; // Unique identifier for the resource
    protected String name; // Name of the resource
    public enum resourceTypes {
            Material(new String[]{"UID", "Name", "Unit Cost", "Quantity"}),
            Human(new String[]{"UID", "Name", "Role", "Hourly Rate", "Hours"}),
            Misc(new String[]{"UID", "Name", "Cost"});
            public final String[] COLUMN_NAMES;
            resourceTypes(String[] cols) {
                this.COLUMN_NAMES = cols;
            }
            public String[] getColumnNames() {
                return COLUMN_NAMES;
            }
    };

    // Constructor
    public Resource(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    // Getters for id and name
    public String getId() {
        return uid;
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

