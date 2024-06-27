package views;

import models.Process;
import models.resources.MaterialResource;
import models.resources.ResourceTableModel;

import javax.swing.*;
import java.awt.*;

public class ProcessFormView extends JFrame {
    private JTextField processName;
    private JTextField totalCost;
    private JTextField totalDuration;
    private JButton saveButton;
    private JTabbedPane resourcesTabbedPane;

    private JPanel createDetailsPanel(){
        JPanel detailsFormPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        detailsFormPanel.add(new JLabel("Process Name:"));
        processName = new JTextField();
        detailsFormPanel.add(processName);

        detailsFormPanel.add(new JLabel("Total Cost:"));
        totalCost = new JTextField();

        detailsFormPanel.add(new JLabel("Total Duration:"));
        totalDuration = new JTextField();

        return detailsFormPanel;
    }
    public ProcessFormView(Process process) {
        setTitle("OMEGA: Process Form");
        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel detailsFormPanel = createDetailsPanel();
        add(detailsFormPanel, BorderLayout.NORTH);

        resourcesTabbedPane = new JTabbedPane();
        String[] resourceTypes = {"Human", "Material", "MISC"}; //todo make enum on the level of resource and add tabs and their content based on that new enum
        for (String resourceType : resourceTypes) {
            resourcesTabbedPane.addTab(resourceType, createProcessPanel(process));
        }
//        resourceTypes.forEach(resourceType -> {
//            resourcesTabbedPane.addTab(resource.getName(), createProcessPanel(resource));
//        });

        add(resourcesTabbedPane, BorderLayout.CENTER);

//        JScrollPane scrollPane = new JScrollPane(resourcesTable);
//        add(scrollPane, BorderLayout.SOUTH);

        // Create new project button
        saveButton = new JButton("Save Process");
        saveButton.addActionListener(e -> {
//            exit the jpanel
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private JPanel createProcessPanel(Process process) {
        JPanel taskPanel = new JPanel(new BorderLayout());


        ResourceTableModel tableModel = new ResourceTableModel(process);
        JTable processTable = new JTable(tableModel);

        // Add some dummy data for illustration purposes
//        tableModel.addRow(new Object[]{false, "1", "Design Specification", "Completed", "$1000", "5 days"});
//        tableModel.addRow(new Object[]{false, "2", "Material Selection", "In Progress", "$500", "3 days"});

        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Resource");
        int[] latestProcessId = {0};
        addRowButton.addActionListener(e -> {
            System.out.println("Add Resource clicked");
//            int newProcessId = latestProcessId[0] + 1;
////            tableModel.addRow(new Object[]{false, newProcessId, "", "", "", ""});
//            process.addResource(new MaterialResource(newProcessId, "", 0.0), 1);
//            latestProcessId[0] = newProcessId; // Update the process ID
        });
        // Create a button for removing selected rows
        JButton removeButton = new JButton("Remove Selected Processes");
        // Add action listener to handle button click for removing rows
        removeButton.addActionListener(e -> {
            // Iterate through selected rows and remove them from the table
//            process.removeResource();
        });
        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRowButton);
        buttonPanel.add(removeButton);

        JScrollPane scrollPane = new JScrollPane(processTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.SOUTH);

        return taskPanel;
    }

    public static void main(String[] args) {
        new ProcessFormView(new Process());
    }
}
