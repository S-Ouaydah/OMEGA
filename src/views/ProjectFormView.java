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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create project details panel
        JPanel projectDetailsPanel = new JPanel(new BorderLayout());
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("Project Details"));
        return projectDetailsPanel;
    }
    private JPanel createImagePanel(Project project) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        prototypeImageLabel = new JLabel();
        prototypeImageLabel.setPreferredSize(new Dimension(200, 200));
        prototypeImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prototypeImageLabel.setIcon(new ImageIcon(project.geImagePath()));
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
                    project.setImagePath(fileChooser.getSelectedFile().getPath());
                    ImageIcon imageIcon = new ImageIcon(project.geImagePath());
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
                project.writeToFile(ProjectListView.PROJECTS_DIR + fileName);

                System.out.println("Project saved successfully!");

                dispose(); // Close views.ProjectListView after saving

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
        JPanel mainPanel = createMainPanel();

        JPanel imagePanel = createImagePanel(projectData);
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
    @Override
    public void dispose() {
        super.dispose();
        new ProjectListView();
    }
    public static void main(String[] args) {
//        new ProjectFormView(loadProject("test.data"));
//        new ProjectFormView(new Project());

    }
}
