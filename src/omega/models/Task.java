package omega.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Task extends Observable implements Serializable {
    public enum Type {
        Design, Preparation, Fabrication, Assembly, Testing
    }
    private String id; // Unique identifier for the task
    private Type taskType; // Type of the task (design, preparation, fabrication, assembly, testing)
    private List<Process> processes; // List of processes associated with the task
    private double cost; // Total cost of the task
    private String status; // Current status of the task (e.g., "In Progress", "Completed")
    private int duration; // Estimated duration of the task in days

    // Constructor
    // todo: fix id
    public Task(Type type) {
//        this.id = id;
        this.taskType = type;
        this.processes = new ArrayList<>();
        this.cost = 0;
        this.status = "In Progress";
        this.duration = 0;
    }

    // Getters and setters for all attributes

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
    public double getCost() { return cost; }
    public int getDuration() { return duration; }
    public void setStatus(String status) {
        this.status = status;
    }
    // Add process to the task
    public Process addProcess(Process process) {
        processes.add(process);
//        todo: fix cost
//        updateCostAndDuration(); // Recalculate cost and duration after adding a process
        setChanged();
        notifyObservers();
        return process;
    }

    // Remove process from the task
    public void removeProcess(Process process) {
        processes.remove(process);
//        todo: fix cost
//        updateCostAndDuration(); // Recalculate cost and duration after removing a process
        setChanged();
        notifyObservers();
    }

    public double calculateCost() {
        double cost = 0;
        for (Process process : processes) {
            cost += process.getCost();
        }
        return cost;
    }
    public int CalculateDuration() {
        int duration = 0;
        for (Process process : processes) {
            duration += process.getDuration();
        }
        return duration;
    }


    public Type getType() {
        return taskType;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public static List<Task> getAllTypes() {
        return new ArrayList<>(List.of(new Task(Type.Design), new Task(Type.Preparation), new Task(Type.Fabrication), new Task(Type.Assembly), new Task(Type.Testing)));
    }

//    public static List<omega.models.Task> getTasksFromUI() {
//        List<omega.models.Task> tasks = new ArrayList<>();
//        // Add tasks from UI to the list
//
//        return tasks;
//    }
}
