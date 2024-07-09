package views;

import models.Project;
import models.Task;
import models.resources.Resource;
import models.Process;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FilterView extends JFrame {
    private Project project;
    private JCheckBox showTasksCheckbox;
    private JCheckBox showProcessesCheckbox;
    private JCheckBox showResourcesCheckbox;
    private JTextField minCostField;
    private JTextField maxCostField;
    private JButton applyFilterButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public FilterView(Project project) {
        this.project = project;
        setTitle("Filter Project Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(5, 2, 10, 10));
        filterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        showTasksCheckbox = new JCheckBox("Show Tasks with Processes");
        showProcessesCheckbox = new JCheckBox("Show Processes");
        showResourcesCheckbox = new JCheckBox("Show Resources");

        JLabel minCostLabel = new JLabel("Min Cost:");
        minCostField = new JTextField();
        minCostField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        JLabel maxCostLabel = new JLabel("Max Cost:");
        maxCostField = new JTextField();
        maxCostField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        applyFilterButton = new JButton("Apply Filter");
        applyFilterButton.addActionListener(e -> applyFilter());

        filterPanel.add(showTasksCheckbox);
        filterPanel.add(new JLabel());
        filterPanel.add(showProcessesCheckbox);
        filterPanel.add(new JLabel());
        filterPanel.add(showResourcesCheckbox);
        filterPanel.add(new JLabel());
        filterPanel.add(minCostLabel);
        filterPanel.add(minCostField);
        filterPanel.add(maxCostLabel);
        filterPanel.add(maxCostField);

        add(filterPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Name", "Cost"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyFilterButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add mouse listener for double-click to open resources or processes
//        resultTable.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                    openResourceOrProcess();
//            }
//        });

        setVisible(true);
    }

    private void applyFilter() {
        double minCost = minCostField.getText().isEmpty() ? 0 : Double.parseDouble(minCostField.getText());
        double maxCost = maxCostField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxCostField.getText());

        tableModel.setRowCount(0);

        if (showTasksCheckbox.isSelected()) {
            for (Task task : project.getTasks()) {
                if (!task.getProcesses().isEmpty()) {
                    double taskCost = task.getProcesses().stream().mapToDouble(Process::getCost).sum();
                    if (taskCost >= minCost && taskCost <= maxCost) {
                        tableModel.addRow(new Object[]{task.getType(), taskCost});
                    }
                }
            }
        }

        if (showProcessesCheckbox.isSelected()) {
            for (Task task : project.getTasks()) {
                for (Process process : task.getProcesses()) {
                    if (process.getCost() >= minCost && process.getCost() <= maxCost) {
                        tableModel.addRow(new Object[]{process.getName(), process.getCost()});
                    }
                }
            }
        }

        if (showResourcesCheckbox.isSelected()) {
            for (Resource resource : project.getResources()) {
                if (resource.getCost() >= minCost && resource.getCost() <= maxCost) {
                    tableModel.addRow(new Object[]{resource.getName(), resource.getCost()});
                }
            }
        }
    }

    // Function to open resource or process details
//    private void openResourceOrProcess() {
//
//    }
}
