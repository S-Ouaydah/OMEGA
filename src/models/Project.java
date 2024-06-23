package models;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class Project implements Serializable{

    @Serial
    private static final long serialVersionUID = 1;
    private int id;
    private String state;
    private double cost;
    private String projectName;
    private String customer;
    private LocalDate date;
    private List<Task> tasks;


    public Project(String projectName, String customer, LocalDate date) {
        this.projectName = projectName;
        this.customer = customer;
        this.date = date;
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
}
