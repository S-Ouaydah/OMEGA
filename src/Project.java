import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Project implements Serializable{

    @Serial
    private static final long serialVersionUID = 2641829790221971234L; // Use the serialVersionUID from the error message
    private int id;
    private String state;
    private double cost;
    private String projectName;
    private String customer;
    private LocalDate date;
    private List<Task> tasks;
    private String[][] processData;


    public Project(String projectName, String customer, LocalDate date) {
        this.projectName = projectName;
        this.customer = customer;
        this.date = date;
        this.processData = new String[0][]; // Initialize empty process data
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
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(this);
        outputStream.close();
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
