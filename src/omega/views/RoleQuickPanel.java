package omega.views;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        rolePay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
        add(rolePayLabel);
        add(rolePay);
    }


}
