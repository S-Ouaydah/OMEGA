import javax.swing.*;

public class ProjectForm extends JFrame {
    private JPanel main_panel;
    private JTextField prototype_name;
    private JTabbedPane prototype_stages;
    private JLabel prototype_image;
    private JScrollPane main_scroll_pane;
    private JPanel conception_tab;
    private JPanel matieres_premieres;
    private JPanel testing;
    private JPanel assemblage;
    private JPanel fabrication;
    private JLabel prototype_name_label;
    private JLabel customer_label;
    private JTextField customer;
    private JTextField price;
    private JLabel price_label;
    private JTable processus_conception;
    private JTable processus_matieres;
    private JTable processus_fabrication;
    private JTable processus_assemblage;
    private JTable processus_testing;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        ImageIcon imageIcon = new ImageIcon("./proto.jpeg");
        prototype_image = new JLabel();
        prototype_image.setIcon(imageIcon);
    }
    public ProjectForm() {
        createUIComponents();
    }
    public static void main(String[] args) {
        JFrame frame = new ProjectForm();
        frame.setContentPane(new ProjectForm().main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
