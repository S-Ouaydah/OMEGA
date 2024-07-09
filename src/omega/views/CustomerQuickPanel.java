package omega.views;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CustomerQuickPanel extends JPanel {
    public JTextField customerNameField, customerEmailField, customerPhoneField;
    public CustomerQuickPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Quick Panel"));

        JLabel customerName = new JLabel("Customer Name:");
        customerNameField = new JTextField();
        add(customerName);
        add(customerNameField);

        JLabel customerEmail = new JLabel("Customer Email:");
        customerEmailField = new JTextField();
        add(customerEmail);
        add(customerEmailField);

        JLabel customerPhone = new JLabel("Customer Phone:");
        customerPhoneField = new JTextField();
        customerPhoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '+') {
                    e.consume();
                }
            }
        });
        add(customerPhone);
        add(customerPhoneField);

    }
}
