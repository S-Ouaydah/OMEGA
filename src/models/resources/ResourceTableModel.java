package models.resources;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class ResourceTableModel extends AbstractTableModel {
    private List<Resource> resources;
    private final String[] COLUMN_NAMES;
    private final int[] NOT_EDITABLE;

    public ResourceTableModel(List<Resource> resources, String[] columnNames, int[] notEditableColumns) {
        this.resources = resources;
        this.COLUMN_NAMES = columnNames;
        this.NOT_EDITABLE = notEditableColumns;
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
        return Arrays.stream(NOT_EDITABLE).noneMatch(i -> i == columnIndex);
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
        fireTableDataChanged();
    }
}

