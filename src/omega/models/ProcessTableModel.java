package omega.models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.parseInt;

public class ProcessTableModel extends AbstractTableModel implements Observer {
    private static final String[] COLUMN_NAMES = {"Select","ID", "Name", "Status","Cost", "Duration"};
    private List<Process> processes;
    public List<Integer> selectedRows = new ArrayList<>();

    public ProcessTableModel(Task task) {
        this.processes = task.getProcesses();
        task.addObserver(this);
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
        return processes.size();
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
        Process process = processes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selectedRows.contains(rowIndex);
            case 1:
                return process.getUid();
            case 2:
                return process.getName();
            case 3:
                return process.getStatus();
            case 4:
                return process.getCost();
            case 5:
                return process.getDuration();
            default:
                return null;
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Process process = processes.get(rowIndex);
        if (columnIndex == 0) {
            if ((boolean) aValue) {
                selectedRows.add(rowIndex);
            } else {
                selectedRows.remove((Integer) rowIndex);
            }
        }
        if (columnIndex == 2) process.setName((String) aValue);
        if (columnIndex == 3) process.setStatus((String) aValue);
        if (columnIndex == 5) process.setDuration(parseInt((String) aValue));
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 ;
    }
    @Override
    public void update(Observable o, Object arg) {
        fireTableDataChanged();
    }
}
