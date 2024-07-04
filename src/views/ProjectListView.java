package views;

import models.Project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ProjectListView extends JFrame {

    private JTable projectTable;
    private JButton newProjectButton;
    private JTextField searchField;
    private ArrayList<String> projectNames = new ArrayList<>();
    public static final String PROJECTS_DIR = "storage/projects/";

    //Constructor
    public ProjectListView() {
        setTitle("Project Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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
            new ProjectFormView(new Project()).setVisible(true); // Create and show new ProjectFormView
            dispose();
        });

        // Layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newProjectButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        ArrayList<String> saved = getSavedProjects(); // Call method to update project list on startup
        if (saved != null) {
            for (String project : saved) {
                tableModel.addRow(new String[]{project});
            }
        }

        setVisible(true);
    }

    //Method to update the project list
    private ArrayList<String> getSavedProjects() {
        DefaultTableModel tableModel = (DefaultTableModel) projectTable.getModel();
        // Add rows from saved project files
        File projectDir = new File(PROJECTS_DIR);
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
        try {
            File projectFile = new File(PROJECTS_DIR + projectName + ".data");
            if (projectFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(projectFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Project project = (Project) objectInputStream.readObject();

                new ProjectFormView(project);

                objectInputStream.close();
                fileInputStream.close();
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Project file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open project.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ProjectListView();
    }
}
