package views;

import models.Customer;
import models.Role;
import models.Project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static utils.Loaders.*;

public class ProjectListView extends JFrame {

    private JTable projectTable;
    private JButton newProjectButton,newCustomerButton, newRoleButton;
    private JTextField searchField;
    private ArrayList<String> projectNames = new ArrayList<>();

    //Constructor
    public ProjectListView() {
        setTitle("Project Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create project table with a DefaultTableModel
        String[] columnNames = {"Project Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        projectTable = new JTable(tableModel);
        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow single project selection

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

        JScrollPane scrollPane = new JScrollPane(projectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize the search field
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProjects();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProjects();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterProjects();
            }
        });

        // Create new project button
        newProjectButton = new JButton("New Project");
        newProjectButton.addActionListener(e -> {
            String projectName = JOptionPane.showInputDialog(this, "Enter project name:");
            if (projectName != null && !projectName.trim().isEmpty()) {
                new ProjectFormView(new Project(projectName));
                dispose();
            }
        });

        newCustomerButton = new JButton("New Customer");
        newCustomerButton.addActionListener(e -> {
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
//                dispose();
            }
        });

        newRoleButton = new JButton("New Role");
        newRoleButton.addActionListener(e -> {
            String newRole = JOptionPane.showInputDialog("Enter new role name:");
            if (newRole != null) {
                double rate = Double.parseDouble(JOptionPane.showInputDialog("Enter hourly rate for " + newRole));
                Role.addRole(new Role(newRole, rate));
            }
            else {
                JOptionPane.showMessageDialog(this, "Can not be empty.");
            }
        });
        // Layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newProjectButton);
        buttonPanel.add(newCustomerButton);
        buttonPanel.add(newRoleButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        ArrayList<String> saved = loadProjects(); // Call method to update project list on startup
        if (saved != null) {
            for (String project : saved) {
                tableModel.addRow(new String[]{project});
            }
        }
        setVisible(true);
    }


    private void filterProjects() {
        DefaultTableModel tableModel = (DefaultTableModel) projectTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        String searchText = searchField.getText().toLowerCase();
        for (String projectName : projectNames) {
            if (projectName.toLowerCase().contains(searchText)) {
                tableModel.addRow(new String[]{projectName});
            }
        }
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

    public static void main(String[] args) {
        loadCustomers();
        loadRoles();
        new ProjectListView();
    }
}
