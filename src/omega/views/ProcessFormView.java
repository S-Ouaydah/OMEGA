package omega.views;

import omega.models.Process;
import omega.models.Role;
import omega.models.resources.*;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public class ProcessFormView extends JFrame implements Observer {
    private JTextField processName;
    private JLabel totalCost;
    private JLabel totalDuration;
    public JButton saveButton;
    public JButton markAsDoneButton;
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel detailsFormPanel = createDetailsPanel(process);
        add(detailsFormPanel, BorderLayout.NORTH);

        resourcesTabbedPane = new JTabbedPane();
        resourcesTabbedPane.addTab("Human", createProcessPanel(process, Resource.resourceTypes.Human, new int[]{0, 3}));
        resourcesTabbedPane.addTab("Material", createProcessPanel(process, Resource.resourceTypes.Material, new int[]{0}));
        resourcesTabbedPane.addTab("Misc", createProcessPanel(process, Resource.resourceTypes.Misc,new int[]{0}));


        add(resourcesTabbedPane, BorderLayout.CENTER);

        // Create new project button
        saveButton = new JButton("Save Process");
        saveButton.addActionListener(e -> {
            process.setName(processName.getText());
            if (!totalCost.getText().isEmpty()) {
                process.setCost(Double.parseDouble(totalCost.getText()));
            }
            if (!totalDuration.getText().isEmpty()){
                process.setDuration(Integer.parseInt(totalDuration.getText()));
            }
            if (processName.getText().isEmpty()) {
                return;
            }
            dispose();
        });
        // Create new mark as done button
        markAsDoneButton = "In Progress".equals(process.getStatus()) ? new JButton("Mark as Done") : new JButton("Mark as In Progress");
        markAsDoneButton.addActionListener(e -> {
            process.toggleStatus();
            markAsDoneButton.setText("In Progress".equals(process.getStatus()) ? "Mark as Done" : "Mark as In Progress");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(markAsDoneButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private JPanel createProcessPanel(Process process, Resource.resourceTypes type, int[] cols) {
        JPanel taskPanel = new JPanel(new BorderLayout());


        ResourceTableModel tableModel = new ResourceTableModel(process.getResources(type), type.getColumnNames(),cols);
        JTable processTable = new JTable(tableModel);
        if (type == Resource.resourceTypes.Human) {
            JComboBox<Role> roleComboBox = new JComboBox<>();
            for (Role role : Role.getRoles()) {
                roleComboBox.addItem(role);
            }
            processTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(roleComboBox));
        }
        tableModel.addTableModelListener(
            e -> {
                process.updateCost();
                process.updateDuration();
            }
        );
        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Resource");
        addRowButton.addActionListener(e -> {
            String newProcessId = UUID.randomUUID().toString();
            String name = JOptionPane.showInputDialog("Enter Resource Name");
            if (name == null || name.isEmpty()) {return;}
            if (process.getResources(type).stream().anyMatch(resource -> resource.getName().equals(name))) {
                JOptionPane.showMessageDialog(this, "Resource already exists!");
                return;
            }
            switch (type) {
                case Human:
                    process.addResource(new HumanResource(newProcessId, name, null,0), 1);
                    break;
                case Material:
                    process.addResource(new MaterialResource(newProcessId, name, 0.0), 1);
                    break;
                case Misc:
                    process.addResource(new MiscResource(newProcessId, name, 0.0), 1);
                    break;
            }
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
                    tableModel.setResources(process.getResources(type));
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

//    public static void main(String[] args) {
//        new ProcessFormView(new Process());
//    }
    public JTextField getProcessName() {
        return processName;
    }
    @Override
    public void update(Observable o, Object arg) {
        Process process = (Process) o;
        totalCost.setText(String.valueOf(process.getCost()));
        totalDuration.setText(String.valueOf(process.getDuration()));
    }
}
