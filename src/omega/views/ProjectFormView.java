package omega.views;

import omega.MainApp;
import omega.models.*;
import omega.models.Process;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;


public class ProjectFormView extends JFrame {
    private JLabel projectNameField;
    private JComboBox<Customer> customerField;
    private JTextField dateField;
    private JLabel expectedCompletionDateField;
    private JLabel totalCostField;
    private JLabel totalDurationField;
    private JLabel prototypeImageLabel;
    private JTabbedPane tasksTabbedPane;
    private Project project;

    private JPanel createMainPanel() {
        setTitle("Project Form");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);


        // Create project details panel
        JPanel projectDetailsPanel = new JPanel(new BorderLayout());
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("Project Details"));
        return projectDetailsPanel;
    }
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //create a button for simulation
        JButton simulateButton = new JButton("Simulate");
        simulateButton.addActionListener(e -> {
            // Simulate the task
            System.out.println("Simulating");
            SimulationView simulationView = new SimulationView(project);
        });
        //create a button for advanced stats
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> {
            // Show advanced stats
            System.out.println("Showing advanced stats");
            FilterView advancedStatsView = new FilterView(project);
        });

        menuPanel.add(filterButton);
        menuPanel.add(simulateButton);
        return menuPanel;
    }
    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        prototypeImageLabel = new JLabel();
        prototypeImageLabel.setPreferredSize(new Dimension(200, 200));
        prototypeImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prototypeImageLabel.setIcon(getAndScaleImage(project.geImagePath()));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 20));
        imagePanel.add(prototypeImageLabel, BorderLayout.CENTER);

        JButton uploadImageButton = new JButton("Upload Image");
        imagePanel.add(uploadImageButton, BorderLayout.SOUTH);
        uploadImageButton.addActionListener(e -> {
            // Handle image upload
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                project.setImagePath(fileChooser.getSelectedFile().getPath());
                prototypeImageLabel.setIcon(getAndScaleImage(project.geImagePath()));
            }
        });
        return imagePanel;
    }
    private ImageIcon getAndScaleImage(String path) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    private JPanel createDetailsPanel() {
        JPanel detailsFormPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        detailsFormPanel.add(new JLabel("Project Name:"));
        projectNameField = new JLabel(project.getProjectName());
        detailsFormPanel.add(projectNameField);

        detailsFormPanel.add(new JLabel("Customer:"));
        customerField = new JComboBox<>();
        for (Customer customer : Customer.getCustomers()) {
            customerField.addItem(customer);
        }
        customerField.setSelectedItem(project.getCustomer());
        customerField.addActionListener(e -> {
            project.setCustomer((Customer) customerField.getSelectedItem());
        });
        detailsFormPanel.add(customerField);

        detailsFormPanel.add(new JLabel("Start Date:"));
        dateField = new JTextField(project.getDate().toString());
        dateField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                try {
                    LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ISO_DATE);
                    project.setDate(date);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Invalid date format (use YYYY-MM-DD).");
                    dateField.requestFocus();
                }
            }
        });
        detailsFormPanel.add(dateField);

        detailsFormPanel.add(new JLabel("Expected Completion Date:"));
        //empty if no processes
        expectedCompletionDateField = project.getTasks().stream().allMatch(task -> task.getProcesses().isEmpty()) ?
                new JLabel("N/A") :
                new JLabel(project.getDate().plusDays(project.getTotalDuration() / 24).toString());
        expectedCompletionDateField.setOpaque(true);
        expectedCompletionDateField.setBackground(Color.white);
        detailsFormPanel.add(expectedCompletionDateField);


        detailsFormPanel.add(new JLabel("Total Cost:"));
        totalCostField = new JLabel(project.getTotalCost() + "");
        totalCostField.setOpaque(true);
        totalCostField.setBackground(Color.white);
        detailsFormPanel.add(totalCostField);

        detailsFormPanel.add(new JLabel("Total Duration:"));
        totalDurationField = new JLabel(project.getTotalDuration() + "h");
        totalDurationField.setOpaque(true);
        totalDurationField.setBackground(Color.white);
        detailsFormPanel.add(totalDurationField);

        return detailsFormPanel;
    }

    private JPanel createTaskPanel(Task task) {
        JPanel taskPanel = new JPanel(new BorderLayout());

        ProcessTableModel processTableModel = new ProcessTableModel(task);
        JTable processTable = new JTable(processTableModel);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = processTable.rowAtPoint(evt.getPoint());
                int col = processTable.columnAtPoint(evt.getPoint());
                if (col != 0) {
                Process selectedProcess = task.getProcesses().get(row);
                ProcessFormView pv = new ProcessFormView(selectedProcess);
                setVisible(false);
                pv.saveButton.addActionListener(e -> {
                    if (pv.getProcessName().getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(pv, "Please enter the name of the process", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    setVisible(true);
                    //is there not a better way to recalculate?
                    totalCostField.setText(project.getTotalCost() + "");
                    totalDurationField.setText(project.getTotalDuration() + "");
                    // should be calculated based on the task total duration and start date
                    expectedCompletionDateField.setText(project.getDate().plusDays(project.getTotalDuration()/24).toString());
                    pv.dispose();
                });
                }
            }
        });


        // Create a button for adding empty rows
        JButton addRowButton = new JButton("Add Process");
        // Track the latest process ID
        int[] latestProcessId = {0};
        addRowButton.addActionListener(e -> {
            String newProcessId = UUID.randomUUID().toString();;
            String name = JOptionPane.showInputDialog("Enter Process Name");
            if (name == null) return; // Cancel button clicked
            ProcessFormView pv = new ProcessFormView(task.addProcess(new Process(newProcessId, name, "InProgress", 0, 0)));
//            task.addProcess(new Process(newProcessId, "", "", 0, 0));
            processTableModel.selectedRows.clear();
            setVisible(false);
            pv.saveButton.addActionListener(ev -> {
                    if (pv.getProcessName().getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(pv, "Please enter the name of the process", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    setVisible(true);
                    //is there not a better way to recalculate?
                    totalCostField.setText(project.getTotalCost() + "");
                    totalDurationField.setText(project.getTotalDuration() + " h");
                    expectedCompletionDateField.setText(project.getDate().plusDays(project.getTotalDuration()/24).toString());
                    pv.dispose();
            });
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
            String fileName = projectNameField.getText().trim(); // Use project name as file name
            try {
                project.writeToFile(fileName);

                System.out.println("Project saved successfully!");

                dispose(); // Close omega.views.ProjectListView after saving

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


    public ProjectFormView(Project projectData) {
        this.project = projectData;
        JPanel mainPanel = createMainPanel();
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.NORTH);

        JPanel imagePanel = createImagePanel();
        JPanel detailsFormPanel = createDetailsPanel();

        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(detailsFormPanel, BorderLayout.CENTER);

        // Create tasks tabbed pane
        tasksTabbedPane = new JTabbedPane();
//        Create Task Panel Process table, with save button
        List<Task> projectTasks = projectData.getTasks();
        if (projectTasks!=null) {
            for (Task task : projectTasks) {
                tasksTabbedPane.addTab(task.getType().toString(), createTaskPanel(task));

            }
        }

        add(mainPanel, BorderLayout.NORTH);
        add(tasksTabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
    @Override
    public void dispose() {
        super.dispose();
        new MainApp();
    }
//    public static void main(String[] args) {
////        new ProjectFormView(loadProject("test.data"));
//        new ProjectFormView(new Project());
//
//    }
}
