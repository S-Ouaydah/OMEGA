import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class ProjectView extends JFrame {
    private final JTextField projectNameInput = new JTextField(20);
    private final JButton addProject = new JButton("Add Project");
    private final JPanel projectPanel = new JPanel(new BorderLayout());
    private JList<String> projectList;
    private DefaultListModel<String> projectListModel;

    public ProjectView() {
        // Initialize the DefaultListModel and the JList with it
        createUIComponents();
        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the project name to the list model
                String projectName = projectNameInput.getText();
                if (!projectName.trim().isEmpty()) {
                    projectListModel.addElement(projectName);
//                    showMessageDialog(null, "Project added successfully!");
                    projectNameInput.setText("");  // Clear the input field after adding
                }
            }
        });
        projectNameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProject.doClick();
            }
        });
        projectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedProject = projectList.getSelectedValue();
                    if (selectedProject != null) {
                        JOptionPane.showMessageDialog(null, "Selected project: " + selectedProject);
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        projectListModel = new DefaultListModel<>();
        projectList = new JList<>(projectListModel);

        // Add components to the projectPanel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Project Name: "));
        inputPanel.add(projectNameInput);
        inputPanel.add(addProject);

        projectPanel.add(inputPanel, BorderLayout.NORTH);
        projectPanel.add(inputPanel);
        projectPanel.add(new JScrollPane(projectList), BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        ProjectView frame = new ProjectView();
        frame.setTitle("Project View");
        frame.setContentPane(frame.projectPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
