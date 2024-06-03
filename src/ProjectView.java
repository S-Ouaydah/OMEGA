import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class ProjectView extends JFrame {
    private JTextField projectNameInput;
    private JButton addProject;
    private JPanel projectPanel;
    private JList list1;
    private JList<String> projectList;
    private DefaultListModel<String> projectListModel;

    public ProjectView() {
        // Initialize the DefaultListModel and the JList with it
        projectListModel = new DefaultListModel<>();
        projectList = new JList<>(projectListModel);

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
        // Create the JList and set its model
        projectListModel = new DefaultListModel<>();
        projectList = new JList<>(projectListModel);

        // Add components to the projectPanel
        JPanel inputPanel = new JPanel();
        inputPanel.add(projectNameInput);
        inputPanel.add(addProject);

        projectPanel.add(inputPanel, BorderLayout.NORTH);
        projectPanel.add(inputPanel);
        projectPanel.add(new JScrollPane(projectList), BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        ProjectView frame = new ProjectView();
        frame.createUIComponents();
        frame.setTitle("Project View");
        frame.setContentPane(frame.projectPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
