package views;

import models.ProcessTableModel;
import models.resources.*;
import models.Process;
import models.Project;
import models.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ProjectFormView extends JFrame {

    private JTextField projectNameField;
    private JTextField customerField;
    private JTextField dateField;
    private JLabel prototypeImageLabel;
    private JTabbedPane tasksTabbedPane;

    private JPanel createMainPanel() {
        setTitle("Project Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create project details panel
        JPanel projectDetailsPanel = new JPanel(new BorderLayout());
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("Project Details"));
        return projectDetailsPanel;
    }
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        prototypeImageLabel = new JLabel();
        prototypeImageLabel.setPreferredSize(new Dimension(200, 200));
        prototypeImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imagePanel.add(prototypeImageLabel, BorderLayout.CENTER);

        JButton uploadImageButton = new JButton("Upload Image");
        imagePanel.add(uploadImageButton, BorderLayout.SOUTH);
        uploadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle image upload
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    ImageIcon imageIcon = new ImageIcon(fileChooser.getSelectedFile().getPath());
                    prototypeImageLabel.setIcon(imageIcon);
                }
            }
        });
        return imagePanel;
    }
    private JPanel createDetailsPanel() {
        JPanel detailsFormPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        detailsFormPanel.add(new JLabel("Project Name:"));
        projectNameField = new JTextField();
        detailsFormPanel.add(projectNameField);

        detailsFormPanel.add(new JLabel("Customer:"));
        customerField = new JTextField();
        detailsFormPanel.add(customerField);

        detailsFormPanel.add(new JLabel("Date:"));
        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        detailsFormPanel.add(dateField);

        return detailsFormPanel;
    }

    private JPanel createTaskPanel(Task task, Project project) {
        JPanel taskPanel = new JPanel(new BorderLayout());

//        String[] columnNames = {"Select","Process ID", "Process Name", "Status", "Cost", "Duration"};
//        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        ProcessTableModel processTableModel = new ProcessTableModel(task);
        JTable processTable = new JTable(processTableModel);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = processTable.rowAtPoint(evt.getPoint());
                int col = processTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    System.out.println("Row: " + row + " Col: " + col);
                }
                if (col != 0) {
//                get the selected process object
                Process selectedProcess = task.getProcesses().get(row);
                new ProcessFormView(selectedProcess);
                }
            }
        });


        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Process");
        // Track the latest process ID
        int[] latestProcessId = {0};
        // Add action listener to handle button click
        addRowButton.addActionListener(e -> {
            int newProcessId = latestProcessId[0] + 1;
            new ProcessFormView(task.addProcess(new Process(newProcessId, "", "", 0, 0)));
//            task.addProcess(new Process(newProcessId, "", "", 0, 0));
            latestProcessId[0] = newProcessId; // Update the process ID
            processTableModel.selectedRows.clear();

        });

        // Create a button for removing selected rows
        JButton removeButton = new JButton("Remove Selected Processes");
        // Add action listener to handle button click for removing rows
        removeButton.addActionListener(e -> {
            // Iterate through all rows (not just selected)
            for (int i = processTableModel.getRowCount() - 1; i >= 0; i--) {
                boolean isSelected = (boolean) processTableModel.getValueAt(i, 0);
                if (isSelected) {
                    task.removeProcess(task.getProcesses().get(i));
                }
            }
            processTableModel.selectedRows.clear();

        });

        //Create a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String fileName = projectNameField.getText() + ".data"; // Use project name as file name

            try {
                project.setProjectName(projectNameField.getText());
                project.setCustomer(customerField.getText());
                project.setDate(LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_DATE));
                project.writeToFile(fileName);

                System.out.println("Project saved successfully!");

//                dispose(); // Close views.ProjectListView after saving

            } catch (IOException ex){
                System.out.println("Error saving project data!");
            }

        });


        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRowButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);

        JScrollPane scrollPane = new JScrollPane(processTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.SOUTH);

        return taskPanel;
    }

    private List<Process> getProcessesFromTable(JTable processTable) {
        List<Process> processes = new ArrayList<>();
        DefaultTableModel tableModel = (DefaultTableModel) processTable.getModel();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            int processId = (int) tableModel.getValueAt(row, 1); // Assuming process ID is in column 1
            String processName = (String) tableModel.getValueAt(row, 2); // Assuming process name is in column 2
            String status = (String) tableModel.getValueAt(row, 3); // Assuming status is in column 3
            double cost = Double.parseDouble((String) tableModel.getValueAt(row, 4)); // Assuming cost is in column 4 (assuming String representation)
            int duration = Integer.parseInt((String) tableModel.getValueAt(row, 5)); // Assuming duration is in column 5 (assuming String representation)
            Process process = new Process(processId, processName, status, duration, cost);
            processes.add(process);
        }
        return processes;
    }

    private static Project loadProject(String fileName) {
        try {
                Project projectData = Project.readFromFile(fileName);
                System.out.println(projectData);
                System.out.println(projectData.getProjectName());
                System.out.println(projectData.getCustomer());
                System.out.println(projectData.getTasks());
                for (Task task : projectData.getTasks()) {
                     System.out.println(task.getType());
                    for (Process process : task.getProcesses()) {
                        System.out.println(process.getName());
                        for (Resource resource : process.getResources()) {
                            System.out.println(resource);
                        }
                    }
                }
                System.out.println("Project loaded successfully!");

                return projectData;
//                projectNameField.setText(projectData.getProjectName());
//                customerField.setText(projectData.getCustomer());
//                dateField.setText(projectData.getDate().format(DateTimeFormatter.ISO_DATE));
//
//                // Loop through loaded tasks and populate tables
//                for (int i = 0; i < tasksTabbedPane.getTabCount(); i++) {
//                    JPanel taskPanel = (JPanel) tasksTabbedPane.getComponentAt(i);
//                    JTable processTable = findProcessTable(taskPanel);
//
//                    if (processTable != null) {
//                        DefaultTableModel tableModel = (DefaultTableModel) processTable.getModel();
//                        tableModel.setRowCount(0); // Clear existing data
//
//                        String taskType = tasksTabbedPane.getTitleAt(i);
//                        List<Task> tasks = projectData.getTasks();
//
//                        for (Task task : tasks) {
//                            if (task.getType().equals(taskType)) {
//                                // Found matching task, populate table
//                                for (Process process : task.getProcesses()) {
//                                    tableModel.addRow(new Object[]{false, process.getId(), process.getName(),
//                                            process.getStatus(), process.getCost(), process.getDuration()});
//                                }
//                                break; // Only populate the table for the matching task
//                            }
//                        }
//                    }
//                }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error loading project data!, launching new project instead.");
            ex.printStackTrace();
            return new Project();
//            JOptionPane.showMessageDialog(this, "Error loading project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ProjectFormView(Project projectData) {
        JPanel mainPanel = createMainPanel();

        JPanel imagePanel = createImagePanel();
        JPanel detailsFormPanel = createDetailsPanel();

        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(detailsFormPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);

        // Create tasks tabbed pane
        tasksTabbedPane = new JTabbedPane();
        add(tasksTabbedPane, BorderLayout.CENTER);

        this.projectNameField.setText(projectData.getProjectName());
        this.customerField.setText(projectData.getCustomer());
        //todo fix date
//        this.dateField.setText(projectData.getDate().format(DateTimeFormatter.ISO_DATE));

//        Create Task Panel Process table, with save button
        List<Task> projectTasks = projectData.getTasks();
        if (projectTasks!=null) {
            for (Task task : projectTasks) {
                tasksTabbedPane.addTab(task.getType().toString(), createTaskPanel(task,projectData));

            }
        }

        add(tasksTabbedPane, BorderLayout.CENTER);

        // Load saved project data on startup
//        if(projectData.getProjectName() !=null){
//            loadProject(projectData.getProjectName()+".data");
//        }
//        else {
//            projectNameField.setText("New Project");
//            customerField.setText("New Customer");
//            dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
//        }

        setVisible(true);
    }
    public static void main(String[] args) {
        new ProjectFormView(loadProject("asdad.data"));
//        new ProjectFormView(new Project());

    }
}
