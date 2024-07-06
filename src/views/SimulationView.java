package views;

import models.Project;
import models.Task;
import models.Process;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationView extends JFrame {
    private Project project;
    private int currentTaskIndex = 0;
    private JLabel taskTypeLabel;
    private JLabel taskStatsLabel;
    private JLabel taskProcessesLabel;
    private JLabel cumulativeProcessesLabel;
    private JButton nextTaskButton;

    public SimulationView(Project project) {
        this.project = project;
        setTitle("Project Progress");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize labels
        taskTypeLabel = new JLabel("Task: ");
        taskStatsLabel = new JLabel("Stats: ");
        taskProcessesLabel = new JLabel("Processes in Task: ");
        cumulativeProcessesLabel = new JLabel("Cumulative Processes: ");

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(taskTypeLabel);
        infoPanel.add(taskStatsLabel);
        infoPanel.add(taskProcessesLabel);
        infoPanel.add(cumulativeProcessesLabel);
        add(infoPanel, BorderLayout.CENTER);

        // Next Task button
        nextTaskButton = new JButton("Next Task");
        nextTaskButton.addActionListener(e -> showNextTask());

        add(nextTaskButton, BorderLayout.SOUTH);

        // Show the first task
        if (!project.getTasks().isEmpty()) {
            showTask(project.getTasks().get(currentTaskIndex));
        }

        setVisible(true);
    }

    private void showTask(Task task) {
        taskTypeLabel.setText("Task: " + task.getType());
        taskStatsLabel.setText("<html>Stats: " + calculateTaskStats(task).replace("\n", "<br>") + "</html>");
        taskProcessesLabel.setText("Processes in Task: " + task.getProcesses().size());
        cumulativeProcessesLabel.setText("Cumulative Processes: " + calculateCumulativeProcesses());
    }

    private String calculateTaskStats(Task task) {
        // Example calculation: cumulative cost and duration
        double cumulativeCost = 0;
        int cumulativeDuration = 0;
        List<Task> tasks = project.getTasks();

        for (int i = 0; i <= currentTaskIndex; i++) {
            for (Process process : tasks.get(i).getProcesses()) {
                cumulativeCost += process.getCost();
                cumulativeDuration += process.getDuration();
            }
        }

        return "Cumulative Cost: " + cumulativeCost + "\nCumulative Duration: " + cumulativeDuration;
    }

    private int calculateCumulativeProcesses() {
        int cumulativeProcesses = 0;
        List<Task> tasks = project.getTasks();

        for (int i = 0; i <= currentTaskIndex; i++) {
            cumulativeProcesses += tasks.get(i).getProcesses().size();
        }

        return cumulativeProcesses;
    }

    private void showNextTask() {
        List<Task> tasks = project.getTasks();
        System.out.println("Current task index: " + currentTaskIndex);
        while (currentTaskIndex < tasks.size()) {
            Task currentTask = tasks.get(currentTaskIndex);
            System.out.println(currentTask.getType());
            System.out.println(tasks.size());

            if (!currentTask.getProcesses().isEmpty()) {
                System.out.println(currentTask.getType());
                System.out.println("Task has processes.");
                showTask(currentTask);
                currentTaskIndex++;
                return;
            }
            System.out.println("Task has no processes.");
            currentTaskIndex++;
        }
        System.out.println("No more tasks with processes.");

        JOptionPane.showMessageDialog(this, "No more tasks with processes.", "Info", JOptionPane.INFORMATION_MESSAGE);
        nextTaskButton.setEnabled(false);
    }
}
