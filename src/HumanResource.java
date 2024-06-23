// Human Resource class
public class HumanResource extends Resource {
    private String specialty; // Employee's specialty
    private String role; // Employee's role
    private double hourlyRate; // Employee's hourly rate

    // Constructor
    public HumanResource(String id, String name, String specialty, String role, double hourlyRate) {
        super(id, name);
        this.specialty = specialty;
        this.role = role;
        this.hourlyRate = hourlyRate;
    }

    // Getters for specialty, role, and hourlyRate
    public String getSpecialty() {
        return specialty;
    }

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
