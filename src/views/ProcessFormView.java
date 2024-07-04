package views;

import models.Process;
import models.resources.*;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ProcessFormView extends JFrame implements Observer {
    private JTextField processName;
    private JLabel totalCost;
    private JLabel totalDuration;
    private JButton saveButton;
    private JTabbedPane resourcesTabbedPane;

    private JPanel createDetailsPanel(Process process){
        JPanel detailsFormPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        detailsFormPanel.add(new JLabel("Process Name:"));
        processName = new JTextField();
        processName.setText(process.getName());
        detailsFormPanel.add(processName);

        detailsFormPanel.add(new JLabel("Total Cost:"));
        totalCost = new JLabel();
        totalCost.setText(String.valueOf(process.getCost()));
        detailsFormPanel.add(totalCost);

        detailsFormPanel.add(new JLabel("Total Duration:"));
        totalDuration = new JLabel();
        totalDuration.setText(String.valueOf(process.getDuration()));
        detailsFormPanel.add(totalDuration);

        return detailsFormPanel;
    }
    public ProcessFormView(Process process) {
        process.addObserver(this);

        setTitle("OMEGA: Process Form");
        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel detailsFormPanel = createDetailsPanel(process);
        add(detailsFormPanel, BorderLayout.NORTH);

        resourcesTabbedPane = new JTabbedPane();
        resourcesTabbedPane.addTab("Human", createProcessPanel(process, Resource.resourceTypes.Human));
        resourcesTabbedPane.addTab("Material", createProcessPanel(process, Resource.resourceTypes.Material));
        resourcesTabbedPane.addTab("Misc", createProcessPanel(process, Resource.resourceTypes.Misc));
//        String[] resourceTypes = {"Human", "Material", "MISC"};
//        for (String resourceType : resourceTypes) {
//            resourcesTabbedPane.addTab(resourceType, createProcessPanel(process));
//        }

        add(resourcesTabbedPane, BorderLayout.CENTER);

//        JScrollPane scrollPane = new JScrollPane(resourcesTable);
//        add(scrollPane, BorderLayout.SOUTH);

        // Create new project button
        saveButton = new JButton("Save Process");
        saveButton.addActionListener(e -> {
            if (processName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the name of the process", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            process.setName(processName.getText());
            if (!totalCost.getText().isEmpty()) {
                process.setCost(Double.parseDouble(totalCost.getText()));
            }
            if (!totalDuration.getText().isEmpty()){
                process.setDuration(Integer.parseInt(totalDuration.getText()));
            }
//          exit the jpanel
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private JPanel createProcessPanel(Process process, Resource.resourceTypes type) {
        JPanel taskPanel = new JPanel(new BorderLayout());


        ResourceTableModel tableModel = new ResourceTableModel(process.getResources(type), type.getColumnNames());
        JTable processTable = new JTable(tableModel);
        tableModel.addTableModelListener(
            e -> {
                process.updateCost();
            }
        );
        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Resource");
        int[] latestProcessId = {0};
        addRowButton.addActionListener(e -> {
            System.out.println("Add Resource clicked");
            int newProcessId = latestProcessId[0] + 1;
            switch (type) {
                case Human:
                    process.addResource(new HumanResource(newProcessId, "", "Dev",0.0), 1);
                    break;
                case Material:
                    process.addResource(new MaterialResource(newProcessId, "", 0.0), 1);
                    break;
                case Misc:
                    process.addResource(new MiscResource(newProcessId, "", 0.0), 1);
                    break;
            }
            latestProcessId[0] = newProcessId; // Update the process ID
            tableModel.setResources(process.getResources(type));
        });
        // Create a button for removing selected rows
        JButton removeButton = new JButton("Remove Selected Processes");
        // Add action listener to handle button click for removing rows
        removeButton.addActionListener(e -> {
            // Iterate through selected rows and remove them from the table
//            process.removeResource();
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                if (processTable.isRowSelected(i)) {
                    process.removeResource(process.getResources(type).get(i));
                }
            }
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

    @Override
    public void update(Observable o, Object arg) {
        Process process = (Process) o;
        totalCost.setText(String.valueOf(process.getCost()));
        totalDuration.setText(String.valueOf(process.getDuration()));
    }
}
