package omega;

import com.sun.tools.javac.Main;
import omega.models.Customer;
import omega.models.Role;
import omega.models.Project;
import omega.views.CustomerQuickPanel;
import omega.views.ProjectFormView;
import omega.views.RoleQuickPanel;
import omega.views.SearchListView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static omega.utils.Loaders.*;

public class MainApp extends JFrame {
//    private static ArrayList<String> projectNames = new ArrayList<>();
//    private static ArrayList<String> roleNames = new ArrayList<>();
//    private static ArrayList<String> customerNames = new ArrayList<>();

    //Constructor
    public MainApp() {
        setTitle("Project Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        SearchListView projectList = new SearchListView("Project",loadProjects());
        JTable projectTable = projectList.getSearchTable();
        // Double click listener for opening existing project
        projectTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int selectedRow = projectTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String projectName = (String) projectTable.getValueAt(selectedRow, 0);
                        openProject(projectName); // Implement method to open existing project from file
                    }
                }
            }
        });
        projectList.getNewButton().addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(this, "Enter project name:");
            if (projectName != null && !projectName.trim().isEmpty()) {
                new ProjectFormView(new Project(projectName));
                dispose();
            }
        });
        //customer Tab
        SearchListView customerList = new SearchListView("Customer", loadCustomers());
        customerList.getNewButton().addActionListener(e -> {
            CustomerQuickPanel qp = new CustomerQuickPanel();
            int customerName = JOptionPane.showConfirmDialog(
                    this,
                    qp,
                    "Creating new customer:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null
            );
            if (customerName == JOptionPane.OK_OPTION) {
                Customer.addCustomer(new Customer(qp.customerNameField.getText(), qp.customerEmailField.getText(), qp.customerPhoneField.getText()));
                customerList.addName(qp.customerNameField.getText());
//                dispose();
            }
        });
        customerList.getSearchTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int selectedRow = customerList.getSearchTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        showCustomerDetails(selectedRow);
                    }
                }
            }
        });
        // Role Tab
        SearchListView roleList = new SearchListView("Role",loadRoles());
        roleList.getNewButton().addActionListener(e -> {
            RoleQuickPanel qp = new RoleQuickPanel();
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    qp,
                    "Creating new Role:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null
            );
            if (confirm == JOptionPane.OK_OPTION) {
                if (qp.roleName.getText().isEmpty() || qp.rolePay.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Role name and pay cannot be empty");
                } else {
                    try {
                        double rate = Double.parseDouble(qp.rolePay.getText());
                        Role.addRole(new Role(qp.roleName.getText(), rate));
                        roleList.addName(qp.roleName.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Pay must be a number");
                    }
                }
//                dispose();
            }
        });
        roleList.getSearchTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int selectedRow = roleList.getSearchTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        showPay(selectedRow);
                    }
                }
            }
        });
        // Layout setup
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Projects", projectList);
        tabbedPane.addTab("Customers", customerList);
        tabbedPane.addTab("Roles", roleList);

        add(tabbedPane);


        setVisible(true);
    }


    private void openProject(String projectName) {
        // Implement logic to open an existing project from a file
        Project project = loadProject(projectName);
        if (project != null) {
            new ProjectFormView(project);
            dispose();
            return;
        }
        JOptionPane.showMessageDialog(this, "Failed to open project.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void showCustomerDetails(int customerIndex) {
        Customer customer = Customer.getCustomers().get(customerIndex);
        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Customer name: " + customer + "\nEmail: " + customer.getEmail() + "\nPhone number: " + customer.getPhone(), "Customer Details", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Failed to get customer details.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void showPay(int roleIndex) {
        Role role = Role.getRoles().get(roleIndex);
        if (role != null) {
            double rate = role.getHourlyRate();
            JOptionPane.showMessageDialog(this, "Role " + role + " has an hourly rate of " + rate, "Role Details", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Failed to get role rate.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainApp frame = new MainApp();
                frame.setLogo();
            }
        });
    }
//    set logo
    private void setLogo() {
        final URL imageResource = Main.class.getClassLoader().getResource("omega/assets/logo2.png");
        final Image image = Toolkit.getDefaultToolkit().getImage(imageResource);
        final Taskbar taskbar = Taskbar.getTaskbar();
        try {
            //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(image);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        try {
            Image windImage = ImageIO.read(getClass().getResource("assets/logo2.png"));
            setIconImage(windImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
