package models;

import java.io.Serializable;
import java.util.HashMap;

public class Employee implements Serializable {
    private String name;
    private String role;
    private double hourlyRate;
    private static HashMap<String, Double> roleRateMap = new HashMap<>();
    private int hours; // Number of hours worked to be used in stats? also add amount paid to employee?

    public static void addRoleRate(String role, double rate) {
        roleRateMap.put(role, rate);
    }
    // Constructor
    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
        this.hourlyRate = determineHourlyRate(role);
    }
    private double determineHourlyRate(String role) {
        if (roleRateMap.containsKey(role)) {
            return roleRateMap.get(role);
        } else {
            return 0;
        }
    }
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getHours() {
        return hours;
    }

    // Setters for name, role, hourlyRate, and hours
    public String setName(String name) {
        return this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public double getCost() {
        return hourlyRate * hours;
    }

    public double getDuration() {
        return hours;
    }
}
