package views;

import models.Process;
import models.Project;
import models.resources.Resource;
import models.resources.ResourceTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProcessFormView extends JFrame {
    private JTextField processName;
    private JTextField test;
    private JButton newResourceButton;
    private JTabbedPane resourcesTabbedPane;
    public ProcessFormView(Process process) {
        setTitle("OMEGA: Process Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        add processName to top
        JPanel detailsFormPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        detailsFormPanel.add(new JLabel("Process Name:"));
        processName = new JTextField();
        detailsFormPanel.add(processName);

        detailsFormPanel.add(new JLabel("test:"));
        test = new JTextField();
        detailsFormPanel.add(test);
        add(detailsFormPanel, BorderLayout.NORTH);


        resourcesTabbedPane = new JTabbedPane();
        String[] taskNames = {"Human", "Material", "MISC"}; //todo make enum on the level of resource and add tabs and their content based on that new enum
        process.getResources().forEach(resource -> {
            resourcesTabbedPane.addTab(resource.getName(), createProcessPanel(resource));
        });

        add(resourcesTabbedPane, BorderLayout.CENTER);

//        JScrollPane scrollPane = new JScrollPane(resourcesTable);
//        add(scrollPane, BorderLayout.SOUTH);

        // Create new project button
        newResourceButton = new JButton("Process");
        newResourceButton.addActionListener(e -> {
            new ProcessFormView(new Process()).setVisible(true); // Create and show new views.ProjectListView
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newResourceButton);
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
        JButton addRowButton = new JButton("Add Process");
        addRowButton.

        JScrollPane scrollPane = new JScrollPane(processTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        return taskPanel;
    }

    public static void main(String[] args) {
        new ProcessFormView(new Process());
    }
}
