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


public class ProjectListView extends JFrame {

    private JTextField projectNameField;
    private JTextField customerField;
    private JTextField dateField;
    private JLabel prototypeImageLabel;
    private JTabbedPane tasksTabbedPane;

    public ProjectListView() {
        setTitle("Project Form");
        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create project details panel
        JPanel projectDetailsPanel = new JPanel(new BorderLayout());
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("Project Details"));

        // Create prototype image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        prototypeImageLabel = new JLabel();
        prototypeImageLabel.setPreferredSize(new Dimension(200, 200));
        prototypeImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imagePanel.add(prototypeImageLabel, BorderLayout.CENTER);

        JButton uploadImageButton = new JButton("Upload Image");
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
        imagePanel.add(uploadImageButton, BorderLayout.SOUTH);

        // Create details form panel
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

        projectDetailsPanel.add(imagePanel, BorderLayout.WEST);
        projectDetailsPanel.add(detailsFormPanel, BorderLayout.CENTER);

        add(projectDetailsPanel, BorderLayout.NORTH);

        // Create tasks tabbed pane
        tasksTabbedPane = new JTabbedPane();
        String[] taskNames = {"Conception", "Preparation", "Fabrication", "Assembly", "Testing"};

        for (String taskName : taskNames) {
            tasksTabbedPane.addTab(taskName, createTaskPanel(taskName));
        }

        add(tasksTabbedPane, BorderLayout.CENTER);

        // Load saved project data on startup
        loadProjectData("st2.data");
        setVisible(true);
    }

    private JPanel createTaskPanel(String taskName) {
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


        // Track the latest process ID
        int[] latestProcessId = {2}; // Start from 2 based on dummy data

        // Add action listener to handle button click
        addRowButton.addActionListener(e -> {
            int newProcessId = latestProcessId[0] + 1;
            tableModel.addRow(new Object[]{false, newProcessId, "", "", "", ""});
            latestProcessId[0] = newProcessId; // Update the process ID
        });

        // Create a button for removing selected rows
        JButton removeButton = new JButton("Remove Selected Processes");

        // Add action listener to handle button click for removing rows
        removeButton.addActionListener(e -> {
            // Iterate through all rows (not just selected)
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                boolean isSelected = (boolean) tableModel.getValueAt(i, 0);
                if (isSelected) {
                    tableModel.removeRow(i);
                }
            }
        });

        //Create a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String fileName = projectNameField.getText() + ".data"; // Use project name as file name

            try {
                // Convert Project object to Project for serialization
                Project projectData = new Project(
                        projectNameField.getText(),
                        customerField.getText(),
                        LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_DATE)
                );

                // Get tasks from the UI
                List<Task> tasks = getTasksFromUI();

                // Add tasks to the project
                projectData.setTasks(tasks);

                // Write project data to the specified file
                projectData.writeToFile(fileName);

                System.out.println("Project saved successfully!");

                dispose(); // Close ProjectListView after saving

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
    private List<Task> getTasksFromUI() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < tasksTabbedPane.getTabCount(); i++) {
            JPanel taskPanel = (JPanel) tasksTabbedPane.getComponentAt(i);
            String taskType = tasksTabbedPane.getTitleAt(i);

            // Extract process data from the task panel
            JTable processTable = findProcessTable(taskPanel); // Find the JTable within the panel

            // Only proceed if a process table is found
            if (processTable != null) {
                List<Process> processes = getProcessesFromTable(processTable);

                // Only add the task if it has at least one selected process
                if (!processes.isEmpty()) {
                    Task task = new Task(taskType); // Assuming Task still uses a String type
                    task.setProcesses(processes);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    private JTable findProcessTable(JPanel panel) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            Component component = panel.getComponent(i);
            if (component instanceof JTable) {
                return (JTable) component;
            }
        }
        return null; // No JTable found in the panel
    }

//    private List<Task> getTasksFromUI() {
//        List<Task> tasks = new ArrayList<>();
//        for (int i = 0; i < tasksTabbedPane.getTabCount(); i++) {
//            JPanel taskPanel = (JPanel) tasksTabbedPane.getComponentAt(i);
//            String taskType = tasksTabbedPane.getTitleAt(i);
//
//            // Extract process data from the task panel (assuming a process table)
//            JTable processTable = (JTable) taskPanel.getComponent(0); // Assuming process table is the first component
//            List<Process> processes = getProcessesFromTable(processTable);
//
//            // Only add the task if it has at least one selected process
//            if (!processes.isEmpty()) {
//                Task task = new Task(taskType);
//                task.setProcesses(processes);
//                tasks.add(task);
//            }
//        }
//        return tasks;
//    }

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

    private void loadProjectData(String fileName) {
        try {
                Project projectData = Project.readFromFile(fileName);
                projectNameField.setText(projectData.getProjectName());
                customerField.setText(projectData.getCustomer());
                dateField.setText(projectData.getDate().format(DateTimeFormatter.ISO_DATE));

                // Loop through loaded tasks and populate tables
                for (int i = 0; i < tasksTabbedPane.getTabCount(); i++) {
                    JPanel taskPanel = (JPanel) tasksTabbedPane.getComponentAt(i);
                    JTable processTable = findProcessTable(taskPanel);

                    if (processTable != null) {
                        DefaultTableModel tableModel = (DefaultTableModel) processTable.getModel();
                        tableModel.setRowCount(0); // Clear existing data

                        String taskType = tasksTabbedPane.getTitleAt(i);
                        List<Task> tasks = projectData.getTasks();

                        for (Task task : tasks) {
                            if (task.getType().equals(taskType)) {
                                // Found matching task, populate table
                                for (Process process : task.getProcesses()) {
                                    tableModel.addRow(new Object[]{false, process.getId(), process.getName(),
                                            process.getStatus(), process.getCost(), process.getDuration()});
                                }
                                break; // Only populate the table for the matching task
                            }
                        }
                    }
                }

                System.out.println("Project loaded successfully!");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error loading project data!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading project: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //    private void loadProjectData(String fileName) {
//        try {
//            Project projectData = Project.readFromFile(fileName);
//            projectNameField.setText(projectData.getProjectName());
//            customerField.setText(projectData.getCustomer());
//            dateField.setText(projectData.getDate().format(DateTimeFormatter.ISO_DATE));
//
//            System.out.println("Project loaded successfully!");
//
//        } catch (IOException | ClassNotFoundException ex) {
//            System.out.println("Error loading project data!");
//            ex.printStackTrace();
//        }
//    }
    public static void main(String[] args) {
        new ProjectListView();

    }
}
