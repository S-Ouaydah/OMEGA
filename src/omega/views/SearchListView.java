package omega.views;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchListView extends JPanel {
    private JTable searchTable;
    private JButton newButton,deleteButton;
    private JTextField searchField;
    private ArrayList<String> listNames = new ArrayList<>();
    

    public SearchListView(String subject, ArrayList<String> names) {
        setLayout(new BorderLayout());
//         Initialize the search field
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {searchFilter();}

            @Override
            public void removeUpdate(DocumentEvent e) {searchFilter();}

            @Override
            public void changedUpdate(DocumentEvent e) {searchFilter();}
        });
        // Create project table with a DefaultTableModel
        String[] columnNames = {subject + " Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        searchTable = new JTable(tableModel);
        searchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow single project selection
        if (names != null) {
            listNames = names;
            for (String name : listNames) {
                tableModel.addRow(new String[]{name});
            }
        }
        newButton = new JButton("New " + subject);
        deleteButton = new JButton("Delete " + subject);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("search:"), BorderLayout.EAST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(searchTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void searchFilter() {
        DefaultTableModel tableModel = (DefaultTableModel) searchTable.getModel();
        tableModel.setRowCount(0); // Clear table
        String searchText = searchField.getText().toLowerCase();
        for (String name : listNames) {
            if (name.toLowerCase().contains(searchText)) {
                tableModel.addRow(new String[]{name});
            }
        }
    }
    public JTable getSearchTable() {
        return searchTable;
    }
    public JButton getNewButton() {
        return newButton;
    }
    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void addName(String name) {
        listNames.add(name);
        searchFilter();
    }
    public void removeName(int index) {
        listNames.remove(index);
        searchFilter();
    }
}
