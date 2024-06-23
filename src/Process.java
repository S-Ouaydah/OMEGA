import java.util.ArrayList;
import java.util.List;

public class Process {
    private int id; // Unique identifier for the process
    private String name; // Name of the process
    private List<Resource> resources; // List of resources assigned to the process
    private double cost; // Cost of the process
    private String status; // Current status of the process ("In Progress", "Completed")
    private int duration; // Estimated duration of the process in days

    // Constructor
    public Process(int id, String name) {
        this.id = id;
        this.name = name;
        this.resources = new ArrayList<>();
        this.cost = 0;
        this.status = "In Progress";
        this.duration = 0;
    }
    public Process(int id, String name, String status ,int duration, double cost) {
        this.id = id;
        this.name = name;
        this.resources = new ArrayList<>();
        this.cost = 0;
        this.status = "In Progress";
        this.duration = 0;
    }

    // Getters and setters for all attributes
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Assign resource to the process
    public void assignResource(Resource resource, int nb) {
        resources.add(resource);
//        updateCost(nb); // Recalculate cost after assigning a resource
    }

    // Remove resource from the process
    public void removeResource(Resource resource, int nb) {
        resources.remove(resource);
//        updateCost(nb); // Recalculate cost after removing a resource
    }

    // Update cost based on resources
    private void updateCost(int nb) {
        cost = 0;
        for (Resource resource : resources) {
            cost += resource.getCost(nb);
        }
    }

    public double getCost() {
        return cost;
    }

    public int getDuration() {
        return duration;
    }
}
