import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class HomeView extends JFrame {

    private JTable projectTable;
    private JButton newProjectButton;

    public HomeView() {
        setTitle("Project Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create project table with a DefaultTableModel
        String[] columnNames = {"Project Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        projectTable = new JTable(tableModel);
        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow single project selection

        // Double click listener for opening existing project
        projectTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int selectedRow = projectTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String projectName = (String) projectTable.getValueAt(selectedRow, 0);
                        openProject(projectName); // Implement method to open existing project from file
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(projectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create new project button
        newProjectButton = new JButton("New Project");
        newProjectButton.addActionListener(e -> {
            new ProjectListView().setVisible(true); // Create and show new ProjectListView
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newProjectButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateProjectList(); // Call method to update project list on startup

        setVisible(true);
    }

    private void updateProjectList() {
        DefaultTableModel tableModel = (DefaultTableModel) projectTable.getModel(); // Cast to DefaultTableModel
        tableModel.setRowCount(0); // Clear existing rows

        // Add rows from existing project files (implement your logic here)
        // You can iterate through project files in a directory and add their names
        // for example:
        File[] projectFiles = new File("projects").listFiles();
        if (projectFiles != null) {
            for (File file : projectFiles) {
                if (file.isFile() && file.getName().endsWith(".data")) {
                    String projectName = file.getName().replace(".data", "");
                    tableModel.addRow(new String[]{projectName});
                }
            }
        }
    }

    public static void updateProjectList(String projectName, JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(new String[]{projectName});
    }


    private void openProject(String projectName) {
        // Implement logic to open an existing project from a file
        // You can load the serialized Project data from the corresponding file
        // and populate the ProjectListView with the loaded data
        System.out.println("Opening project: " + projectName); // Placeholder for actual logic
    }

    public static void main(String[] args) {
        new HomeView();
    }
}
