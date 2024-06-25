package views;

import models.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ProjectListView extends JFrame {

    private JTable projectTable;
    private JButton newProjectButton;
    private static final String PROJECTS_DIR =  "src" ;


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

        // Create new project button
        newProjectButton = new JButton("New Project");
        newProjectButton.addActionListener(e -> {
            new ProjectFormView(new Project()).setVisible(true); // Create and show new views.ProjectListView
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newProjectButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateProjectList(); // Call method to update project list on startup

        setVisible(true);
    }

    //Method to update the project list
    private void updateProjectList() {
        DefaultTableModel tableModel = (DefaultTableModel) projectTable.getModel(); // Cast to DefaultTableModel
        tableModel.setRowCount(0); // Clear existing rows

        //dummy data
        tableModel.addRow(new String[]{"st2"});
        // Add rows from existing project files in the src directory
        File projectDir = new File(PROJECTS_DIR);
        File[] projectFiles = projectDir.listFiles();
        if (projectFiles != null) {
            for (File file : projectFiles) {
                if (file.isFile() && file.getName().endsWith(".data")) {
                    String projectName = file.getName().replace(".data", "");
                    tableModel.addRow(new String[]{projectName});
                }
            }
        }
    }


    private void openProject(String projectName) {
        // Implement logic to open an existing project from a file
        try {
            File projectFile = new File( projectName + ".data");
            if (projectFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(projectFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Project project = (Project) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();

                // Open ProjectFormView with the loaded project data
                new ProjectFormView(project).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Project file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open project.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Opening project: " + projectName); // Placeholder for actual logic
    }

    public static void main(String[] args) {
        new ProjectListView();
    }
}
