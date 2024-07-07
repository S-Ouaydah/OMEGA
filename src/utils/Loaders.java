package utils;

import models.Customer;
import models.Role;
import models.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Loaders {
    public static ArrayList<String> loadProjects() {
        ArrayList<String> projectNames = new ArrayList<>();
        File projectDir = new File("storage/projects");
        File[] projectFiles = projectDir.listFiles();
        if (projectFiles != null) {
            for (File file : projectFiles) {
                if (file.isFile() && file.getName().endsWith(".data")) {
                    String projectName = file.getName().replace(".data", "");
                    projectNames.add(projectName);
                }
            }
            return projectNames;
        }
        else return null;
    }
    public static Project loadProject(String projectName) {
        try {
            File projectFile = new File("storage/projects/" + projectName + ".data");
            if (projectFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(projectFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Project project = (Project) objectInputStream.readObject();

                objectInputStream.close();
                fileInputStream.close();
                return project;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void loadCustomers() {
        try {
            FileInputStream fileInputStream = new FileInputStream("storage/customers/customers.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Customer> customers = (ArrayList<Customer>) objectInputStream.readObject();
            Customer.initCustomers(customers);
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void loadRoles() {
        try {
            FileInputStream fileInputStream = new FileInputStream("storage/roles/roles.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Role.initRoles((ArrayList<Role>) objectInputStream.readObject());
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
//            print current directory
            System.out.println(System.getProperty("user.dir"));
        }
    }
}
