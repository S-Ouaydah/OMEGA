package models.resources;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class HumanResource extends Resource implements Serializable {
    private String role; // Employee's role
    private double hourlyRate; // Employee's hourly rate
    private int hours; // Number of hours worked
    public static HashMap<String,Double> RoleRates = new HashMap<>();
    public static HashMap<String,String> EmployeeRoles = new HashMap<>();

    // Constructor
    public HumanResource(int id, String name, String role, double hourlyRate) {
        super(id, name);
        this.role = role;
        this.hourlyRate = hourlyRate;
        RoleRates.put(role,hourlyRate);
    }
//    constructor for new human resource using existing rates
    public HumanResource(int id, String name, String role) {
        super(id, name);
        this.role = role;
        this.hourlyRate = RoleRates.get(role);
    }

    // Getters for role, and hourlyRate

    public String getRole() {
        return role;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    @Override
    public double getCost() {
        return hourlyRate * hours;
    }

    public int getHours() {
        return hours;
    }
    public void setRole(String role) {
        this.role = role;
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
            case 2 -> role = ((String) aValue);
            case 3 -> hourlyRate = parseDouble((String) aValue);
            case 4 -> hours = parseInt((String) aValue);
        }
    }
}
