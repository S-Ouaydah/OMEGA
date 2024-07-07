package views;

import models.Role;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RoleQuickPanel extends JPanel {
    JTextField employeeName;
    JComboBox<Role> employeeRole;

    JButton addNewRoleButton;

    public RoleQuickPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Quick Panel"));

        JLabel EmployeeNameLabel = new JLabel("Employee Name:");
        employeeName = new JTextField();
        add(EmployeeNameLabel);
        add(employeeName);

        JLabel EmployeeRoleLabel = new JLabel("Employee Role:");
        ArrayList<Role> roles = Role.getRoles();
        employeeRole = new JComboBox<>();
        for (Role role : roles) {
            employeeRole.addItem(role);
        }
        add(EmployeeRoleLabel);
        add(employeeRole);

        addNewRoleButton = new JButton("Add New Role");
        add(addNewRoleButton);
        addNewRoleButton.addActionListener(e -> {
            String roleName = JOptionPane.showInputDialog("Enter new role name:");
            if (roleName != null) {
                double rate = Double.parseDouble(JOptionPane.showInputDialog("Enter hourly rate for " + roleName));
                Role role = new Role(roleName, rate);
                Role.addRole(role);
                employeeRole.addItem(role);
            }
        });
    }


}
