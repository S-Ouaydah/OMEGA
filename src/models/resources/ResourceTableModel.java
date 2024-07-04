package models.resources;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class ResourceTableModel extends AbstractTableModel {
    private List<Resource> resources;
    private final String[] COLUMN_NAMES;

    public ResourceTableModel(List<Resource> resources, String[] columnNames) {
        this.resources = resources;
        this.COLUMN_NAMES = columnNames;
    }

    @Override
    public int getRowCount() {
        return resources.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Resource resource = resources.get(rowIndex);
        return resource.getVal(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Resource resource = resources.get(rowIndex);
        resource.setVal(columnIndex,aValue);
        fireTableDataChanged();
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
        fireTableDataChanged();
    }
}

