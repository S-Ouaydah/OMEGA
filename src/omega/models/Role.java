package omega.models;

import java.io.*;
import java.util.ArrayList;

public class Role implements Serializable {
    private String name;
    private double hourlyRate;
    private static ArrayList<Role> roles = new ArrayList<>();
    public static void initRoles(ArrayList<Role> roles) {
        Role.roles = roles;}
    public static ArrayList<Role> getRoles() {return roles;}
    public static void addRole(Role role) {
        roles.add(role);
        saveRoles();
    }
    public static void removeRole(Role role) {
        roles.remove(role);
        saveRoles();
    }
    public static void saveRoles() {
        try {
            File file = new File("storage/roles");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream("storage/roles/roles.data");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(roles);
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Constructor
    public Role(String name, Double rate) {
        this.name = name;
        this.hourlyRate = rate;
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Role && ((Role) obj).name.equals(name) && ((Role) obj).hourlyRate == hourlyRate;
    }

    public double getHourlyRate() {return hourlyRate;}
}
