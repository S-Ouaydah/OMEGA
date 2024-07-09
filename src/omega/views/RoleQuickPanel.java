package omega.views;

import javax.swing.*;

public class RoleQuickPanel extends JPanel {
    public JTextField roleName;
    public JTextField rolePay;
    public RoleQuickPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel roleNameLabel = new JLabel("Role Name:");
        roleName = new JTextField();
        add(roleNameLabel);
        add(roleName);

        JLabel rolePayLabel = new JLabel("Role Pay:");
        rolePay = new JTextField();
        add(rolePayLabel);
        add(rolePay);
    }


}
