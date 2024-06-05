import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GptForm extends JFrame {

    private JTextField projectNameField;
    private JTextField customerField;
    private JTextField dateField;
    private JTextField someting;
    private JLabel prototypeImageLabel;
    private JTabbedPane tasksTabbedPane;

    public GptForm() {
        setTitle("Project Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create project details panel
        JPanel projectDetailsPanel = new JPanel(new BorderLayout());
        projectDetailsPanel.setBorder(BorderFactory.createTitledBorder("Project Details"));

        // Create prototype image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        prototypeImageLabel = new JLabel();
        prototypeImageLabel.setPreferredSize(new Dimension(250, 250));
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
        JPanel detailsFormPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        detailsFormPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        projectNameField = new JTextField();
        detailsFormPanel.add(new JLabel("Project Name:"));
        detailsFormPanel.add(projectNameField);


        customerField = new JTextField();
        detailsFormPanel.add(new JLabel("Customer:"));
        detailsFormPanel.add(customerField);

        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        detailsFormPanel.add(new JLabel("Date:"));
        detailsFormPanel.add(dateField);

        someting = new JTextField();
        detailsFormPanel.add(new JLabel("someting:"));
        detailsFormPanel.add(someting);

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

        setVisible(true);
    }

    private JPanel createTaskPanel(String taskName) {
        JPanel taskPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Process ID", "Process Name", "Status", "Cost", "Duration"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable processTable = new JTable(tableModel);

        // Add some dummy data for illustration purposes
        tableModel.addRow(new Object[]{"1", "Design Specification", "Completed", "$1000", "5 days"});
        tableModel.addRow(new Object[]{"2", "Material Selection", "In Progress", "$500", "3 days"});

        JScrollPane scrollPane = new JScrollPane(processTable);
        taskPanel.add(scrollPane, BorderLayout.CENTER);

        return taskPanel;
    }

    public static void main(String[] args) {
        new GptForm();
    }
}
