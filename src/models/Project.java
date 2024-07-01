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
    private double cost;
    private State state;
    private String projectName;
    private String customer;
    private LocalDate date;
    private List<Task> tasks;
    private String imagePath;

    public Project() {
        this.state = State.Pending;
        this.tasks = Task.getAllTypes();
    }
    public Project(String projectName, String customer, LocalDate date) {
        this.projectName = projectName;
        this.customer = customer;
        this.date = date;
        this.state = State.Pending;
        this.tasks = Task.getAllTypes();

    }

    //Getters and setters

    //    ====  Getters
    public String getProjectName() {
        return projectName;
    }

    public String getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }
    //  ====  Setters
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void writeToFile(String fileName) throws IOException {
        System.out.println("Writing to file...");
        System.out.println(this.projectName);
        System.out.println(this.customer);
        System.out.println(this.tasks);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(this);
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Project readFromFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
        Project projectData = (Project) inputStream.readObject();
        inputStream.close();
        return projectData;
    }

    // task methods
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public String geImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }
}
