package views;

import models.Project;
import models.Task;
import models.Process;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class SimulationView extends JFrame {
    private Project project;
    private int currentTaskIndex = 0;
    private JLabel taskTypeLabel;
    private JLabel statsLabel;
    private JButton nextTaskButton;

    public SimulationView(Project project) {
        this.project = project;
        setTitle("Project Progress");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize labels
        taskTypeLabel = new JLabel("Task: ");
        statsLabel = new JLabel("<html>Stats:<br>Processes in Task:<br>Cumulative Processes:<br>Tasks Completed:<br>Remaining Cost:<br>Remaining Duration:</html>");

        // Create an info panel with padding
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add padding to the labels
        taskTypeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsLabel.setBorder(new TitledBorder("Stats"));

        infoPanel.add(taskTypeLabel);
        infoPanel.add(statsLabel);
        add(infoPanel, BorderLayout.CENTER);

        // Next Task button
        nextTaskButton = new JButton("Next Task");
        nextTaskButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextTaskButton.setPreferredSize(new Dimension(120, 40));
        nextTaskButton.addActionListener(e -> showNextTask());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(nextTaskButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Show the first task
        if (!project.getTasks().isEmpty()) {
            showTask(project.getTasks().get(currentTaskIndex));
        }

        setVisible(true);
    }

    private void showTask(Task task) {
        taskTypeLabel.setText("Task: " + task.getType());
        statsLabel.setText("<html>" +
                "Processes in Task: " + task.getProcesses().size() + "<br>" +
                "Cumulative Processes: " + calculateCumulativeProcesses() + "<br>" +
                "Tasks Completed: " + calculateTasksCompleted() + "<br>" +
                "Remaining Cost: " + calculateRemainingCost() + "<br>" +
                "Remaining Duration: " + calculateRemainingDuration() +
                "</html>");
    }

    private int calculateCumulativeProcesses() {
        int cumulativeProcesses = 0;
        List<Task> tasks = project.getTasks();

        for (int i = 0; i <= currentTaskIndex; i++) {
            cumulativeProcesses += tasks.get(i).getProcesses().size();
        }

        return cumulativeProcesses;
    }

    private int calculateTasksCompleted() {
        return currentTaskIndex;
    }

    private double calculateRemainingCost() {
        double remainingCost = 0;
        List<Task> tasks = project.getTasks();

        for (int i = currentTaskIndex; i < tasks.size(); i++) {
            for (Process process : tasks.get(i).getProcesses()) {
                remainingCost += process.getCost();
            }
        }

        return remainingCost;
    }

    private int calculateRemainingDuration() {
        int remainingDuration = 0;
        List<Task> tasks = project.getTasks();

        for (int i = currentTaskIndex; i < tasks.size(); i++) {
            for (Process process : tasks.get(i).getProcesses()) {
                remainingDuration += process.getDuration();
            }
        }

        return remainingDuration;
    }

    private void showNextTask() {
        List<Task> tasks = project.getTasks();
        while (currentTaskIndex < tasks.size()) {
            Task currentTask = tasks.get(currentTaskIndex);

            if (!currentTask.getProcesses().isEmpty()) {
                showTask(currentTask);
                currentTaskIndex++;
                return;
            }
            currentTaskIndex++;
        }

        JOptionPane.showMessageDialog(this, "No more tasks with processes.", "Info", JOptionPane.INFORMATION_MESSAGE);
        nextTaskButton.setEnabled(false);
    }
}
