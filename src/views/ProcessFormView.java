package views;

import models.Process;
import models.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProcessFormView extends JFrame {
    private JTable resourcesTable;
    private JButton newResourceButton;
    private JTabbedPane resourcesTabbedPane;
    public ProcessFormView(Process process) {
        setTitle("OMEGA: Process Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"Resource Name", "Resource Type", "Resource Cost"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        resourcesTable = new JTable(tableModel);
        resourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        resourcesTabbedPane = new JTabbedPane();
        String[] taskNames = {"Human", "Material", "MISC"};
        for (String taskName : taskNames) {
            resourcesTabbedPane.addTab(taskName, createProcessPanel(taskName));
        }
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
    private JPanel createProcessPanel(String taskName) {
        JPanel taskPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Select","Process ID", "Process Name", "Status", "Cost", "Duration"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        JTable processTable = new JTable(tableModel) {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    default:
                        return super.getColumnClass(column);
                }
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 1; // Disable editing for (Process ID)
            }
        };
        processTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Allow multiple row selection

        // Add some dummy data for illustration purposes
        tableModel.addRow(new Object[]{false, "1", "Design Specification", "Completed", "$1000", "5 days"});
        tableModel.addRow(new Object[]{false, "2", "Material Selection", "In Progress", "$500", "3 days"});

        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Process");


        JScrollPane scrollPane = new JScrollPane(processTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        return taskPanel;
    }

    public static void main(String[] args) {
        new ProcessFormView(new Process());
    }
}
