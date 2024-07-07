package models;

import views.ProjectFormView;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer implements Serializable {
    private String name;
    private String email;
    private String phone;
//    add demo data to customers
    private static ArrayList<Customer> customers = new ArrayList<>();

    @Override
    public String toString() {
        return this.name;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return Objects.equals(name, customer.name) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(phone, customer.phone);
    }

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public static ArrayList<Customer> getCustomers() {
        return customers;
    }
    public static void initCustomers(ArrayList<Customer> customers) {
        Customer.customers = customers;
    }
    public static void addCustomer(Customer customer) {
        customers.add(customer);
        try {
//            create folder if it does not exist
            File file = new File("storage/customers");
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream("storage/customers/customers.data");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(customers);
            outputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
