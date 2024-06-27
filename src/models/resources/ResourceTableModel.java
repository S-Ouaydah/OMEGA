package models.resources;

import models.Process;
import models.Task;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.parseInt;

public class ResourceTableModel extends AbstractTableModel implements Observer {
    private static final String[] COLUMN_NAMES = {"Resource ID", "Rescource Name", "Cost", "Duration"};
    private List<Resource> resources;
    public List<Integer> selectedRows = new ArrayList<>();

    public ResourceTableModel(Process process) {
        this.resources = process.getResources();
        process.addObserver(this);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            default:
                return super.getColumnClass(column);
        }
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
        switch (columnIndex) {
            case 0:
                return resource.getId();
            case 1:
                return resource.getName();
            case 2:
                return resource.getCost();
            case 3:
                return resource.getDuration();
            default:
                return null;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Resource resource = resources.get(rowIndex);
        if (columnIndex == 0) {
            if ((boolean) aValue) {
                selectedRows.add(rowIndex);
            } else {
                selectedRows.remove((Integer) rowIndex);
            }
        }
        if (columnIndex == 2) resource.setName((String) aValue);
        if (columnIndex == 4) resource.setCost(parseInt((String) aValue));
        if (columnIndex == 5) resource.setDuration(parseInt((String) aValue));
    }


        @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }
    @Override
    public void update(Observable o, Object arg) {
        fireTableDataChanged();
    }
}

