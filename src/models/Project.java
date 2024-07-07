package models;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Project implements Serializable{
    public enum State {
        Pending, In_Progress, Completed
    }
    @Serial
    private static final long serialVersionUID = 1;
    private double totalCost;
    private State state; //use this?
    private String projectName;
    private Customer customer;
    private LocalDate date;
    private List<Task> tasks;
    private String imagePath;

    public Project() {
        this.state = State.Pending;
        this.date = LocalDate.now();
        this.tasks = Task.getAllTypes();
    }
    public Project(String projectName) {
        this.state = State.Pending;
        this.projectName = projectName;
        this.date = LocalDate.now();
        this.tasks = Task.getAllTypes();

    }

    //Getters and setters

    //    ====  Getters
    public String getProjectName() {
        return projectName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }
    //  ====  Setters
    public List<Task> getTasks() {
        return tasks;
    }

    public String geImagePath() {
        return imagePath;
    }


    public double getTotalCost() {
        this.totalCost = 0;
        for (Task task : tasks) {
            totalCost += task.calculateCost();
        }
        return totalCost;
    }
    public int getTotalDuration() {
        int totalDuration = 0;
        for (Task task : tasks) {
            totalDuration += task.CalculateDuration();
        }
        return totalDuration;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    // task methods
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public void writeToFile(String fileName) throws IOException {
        System.out.println("Writing to file...");
        System.out.println(this.projectName);
        try {
//            make directory if it does not exist
            File file = new File("storage/projects");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream("storage/projects/" + fileName + ".data");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(this);
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
