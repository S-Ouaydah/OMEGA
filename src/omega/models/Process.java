package omega.models;

import omega.models.resources.HumanResource;
import omega.models.resources.MaterialResource;
import omega.models.resources.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Process extends Observable implements Serializable {
    private String uid; // Unique identifier for the process
    private String name; // Name of the process
    private List<Resource> resources; // List of resources assigned to the process
    private double cost; // Cost of the process
    private String status; // Current status of the process ("In Progress", "Completed")
    private int duration; // Estimated duration of the process in days
    private String taskType; // Type of the task (design, preparation, fabrication, assembly, testing)

    // Constructor
    public Process() {
        this.resources = new ArrayList<>();
        this.cost = 0;
        this.status = "In Progress";
        this.duration = 0;
    }
    public Process(String uid, String name, String status ,int duration, double cost) {
        this.uid = uid;
        this.name = name;
        this.resources = new ArrayList<>();
        this.cost = cost;
        this.status = status;
        this.duration = duration;
    }

    // Getters and setters for all attributes
    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public double getCost() {
        return cost;
    }
    public int getDuration() { return duration; }
    public List<Resource> getResources() {
        return resources;
    }
    public List<Resource> getResources(Resource.resourceTypes type) { //todo fix this ugly
        List<Resource> resourceList = new ArrayList<>();
            switch (type) {
                case Material -> {
                    for (Resource resource : resources) {
                        if (resource instanceof MaterialResource) {
                            resourceList.add(resource);
                        }
                    }
                }
                case Human -> {
                    for (Resource resource : resources) {
                        if (resource instanceof HumanResource) {
                            resourceList.add(resource);
                        }
                    }
                }
                case Misc -> {
                    for (Resource resource : resources) {
                        if (!(resource instanceof MaterialResource) && !(resource instanceof HumanResource)) {
                            resourceList.add(resource);
                        }
                    }
                }
        }
        return resourceList;
    }
    // Assign resource to the process
    public void addResource(Resource resource, int nb) {
        resources.add(resource);
//        updateCost(); // Recalculate cost after assigning a resource
        setChanged();
        notifyObservers();
    }

    // Remove resource from the process
    public void removeResource(Resource resource) {
        resources.remove(resource);
//        updateCost(); // Recalculate cost after removing a resource
        setChanged();
        notifyObservers();
    }

    // Update cost based on resources
    public void updateCost() {
        cost = 0;
        for (Resource resource : resources) {
            cost += resource.getCost();
        }
        System.out.println("Cost updated" + cost);
        setChanged();
        notifyObservers();
    }
    public void updateDuration() {
        duration = 0;
        for (Resource resource : resources) {
            if (resource instanceof HumanResource){
                duration += ((HumanResource) resource).getHours();
            }
        }
        System.out.println("Duration updated" + duration);
        setChanged();
        notifyObservers();
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(String status) {
        this.status = status;
    }
     public void setDuration(int duration) {
          this.duration = duration;
     }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public void toggleStatus() {
        if ("In Progress".equals(status)) {
            status = "Completed";
        } else {
            status = "In Progress";
        }
        setChanged();
        notifyObservers();
    }

}
