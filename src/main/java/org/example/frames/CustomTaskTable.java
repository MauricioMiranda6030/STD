package org.example.frames;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Task;
import org.example.service.TaskService;
import org.example.util.SoundMaker;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CustomTaskTable extends AbstractTableModel {

    @Getter @Setter
    private List<Task> tasks;
    private final String[] cols = {"Task", "Status"};
    private final TaskService taskService = TaskService.getInstance();

    public CustomTaskTable(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return switch (columnIndex) {
            case 0 -> tasks.get(rowIndex).getName();
            case 1 -> tasks.get(rowIndex).getStatus();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column){
        return cols[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Boolean.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col){
        if(col == 1){
            if(tasks.get(row).getStatus() == false)
                SoundMaker.getInstance().makeSound("/sound/DONE_PS2_OST.wav");
            taskService.updateStatus(row);
            tasks = taskService.getTasks();
            refresh();
        }
    }

    public void refresh(){
        fireTableDataChanged();
    }
}
