package omega.models.resources;

import omega.models.Role;

import java.io.*;

import static java.lang.Integer.parseInt;

public class HumanResource extends Resource implements Serializable {
    private Role role; // Employee's role
    private int hours; // Number of hours worked
//    public static HashMap<String,Double> RoleRates = new HashMap<>();
//    public static HashMap<String,String> EmployeeRoles = new HashMap<>();

    // Constructor
    public HumanResource(int id, String name, Role role, int hours) {
        super(id, name);
        this.role = role;
        this.hours = hours;
    }
    // Getters for role, and hourlyRate
    public Role getRole() {
        return role;
    }

    public double getHourlyRate() {
        if (role == null) return 0;
        return role.getHourlyRate();
    }

    @Override
    public double getCost() {
        return getHourlyRate() * hours;
    }

    public int getHours() {
        return hours;
    }
    @Override
    public Object getVal(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> getId();
            case 1 -> getName();
            case 2 -> getRole();
            case 3 -> getHourlyRate();
            case 4 -> getHours();
            default -> null;
        };
    }
    @Override
    public void setVal(int columnIndex, Object aValue) {
        switch (columnIndex) {
            case 1 -> name = ((String) aValue);
            case 2 -> role = ((Role) aValue);
//            case 3 -> hourlyRate = parseDouble((String) aValue);
            case 4 -> hours = parseInt((String) aValue);
        }
    }
}
