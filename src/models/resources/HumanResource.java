package models.resources;

import java.io.Serializable;

public class HumanResource extends Resource implements Serializable {
    private String role; // Employee's role
    private double hourlyRate; // Employee's hourly rate
    private int hours; // Number of hours worked

    // Constructor
    public HumanResource(int id, String name, String role, double hourlyRate) {
        super(id, name);
        this.role = role;
        this.hourlyRate = hourlyRate;
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
}
