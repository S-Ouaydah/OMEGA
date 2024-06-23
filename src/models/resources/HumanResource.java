package models.resources;

import java.io.Serializable;

public class HumanResource extends Resource implements Serializable {
    private String role; // Employee's role
    private double hourlyRate; // Employee's hourly rate

    // Constructor
    public HumanResource(String id, String name, String role, double hourlyRate) {
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
    public double getCost(int hours) {
        // Calculate cost based on hourly rate
        return hourlyRate * hours;
    }
}
