package org.example.frames;

import org.example.entity.Task;
import org.example.service.TaskService;
import org.example.util.ScreenSize;
import org.example.util.TokyoNightColors;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddTaskFrame extends JFrame{

    private final static int X_VALUE = 310, Y_VALUE = 125;

    private JLabel nameTask;

    private JTextField taskTxt;

    private JButton addBtn;

    private JPanel tabPnl;

    private final TaskService taskService = TaskService.getInstance();

    public AddTaskFrame(TasksFrame tasksFrame){

        tabPnl = new TabPnl(this, X_VALUE);

        nameTask = new JLabel("Task");
        nameTask.setFont(new Font("Roboto", Font.BOLD, 16));
        nameTask.setBounds(5, 52, 40, 20);
        nameTask.setForeground(Color.WHITE);

        taskTxt = new JTextField();
        taskTxt.setFont(new Font("Roboto", Font.PLAIN, 15));
        taskTxt.setBounds(50, 50, 250, 25);
        taskTxt.setForeground(Color.WHITE);
        taskTxt.setBackground(TokyoNightColors.BG_ALT);
        taskTxt.setBorder(new MatteBorder(0,0,1,0,Color.WHITE));

        addBtn = new JButton("<html>ADD</html>");
        addBtn.setBounds(225, 90, 75, 25);
        addBtn.setFont(new Font("Roboto", Font.BOLD, 15));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(TokyoNightColors.GREEN);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(new MatteBorder(1,1,1,1,TokyoNightColors.FG));
        addBtn.addActionListener(ae -> {
            if(!taskTxt.getText().isEmpty()){
                Task task = new Task(taskTxt.getText(), false);
                taskService.addTask(task);
                tasksFrame.refreshList();
                taskTxt.setText("");
            }
        });

        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addBtn.setBackground(TokyoNightColors.DARK_GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addBtn.setBackground(TokyoNightColors.GREEN);
                addBtn.setForeground(Color.WHITE);
            }
        });

        this.setUndecorated(true);
        this.setTitle("Simple To Do");
        this.setSize(X_VALUE, Y_VALUE);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(TokyoNightColors.BG);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        int x = (int) (ScreenSize.WIDTH * 0.593);
        int y = (int) (ScreenSize.HEIGHT * 0.292);

        this.setLocation(x,y);
        this.setVisible(true);
        this.add(tabPnl);
        this.add(nameTask);
        this.add(taskTxt);
        this.add(addBtn);
    }
}
