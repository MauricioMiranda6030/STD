package org.example.frames;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Config;
import org.example.service.PersistService;
import org.example.service.ResetService;
import org.example.service.TaskService;
import org.example.util.SoundMaker;
import org.example.util.TokyoNightColors;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Objects;

public class TasksFrame extends JFrame{

    private static final int X_VALUE = 335, Y_VALUE = 420;
    @Getter @Setter
    private Config config;
    private static Integer hour, min, sec;

    private JButton addBtn, confBtn;

    private CustomTaskTable model;

    private JTable tasksList;

    private final JLabel clockLbl, titleLbl;

    private JFrame addFrame, confFrame;

    private final Timer timer = createTimer();

    private final TaskService taskService = TaskService.getInstance();

    public TasksFrame(){
        PersistService persist = PersistService.getInstance();
        persist.createDir();
        config = persist.getConf();

        setUpConfBtn();
        JPanel tabPnl = new TabPnl(this, X_VALUE);
        tabPnl.add(confBtn);

        config = persist.getConf();
        clockLbl = new JLabel(formatTime(config.getTimerHour(),config.getTimerMin(),0));
        titleLbl = setTitleLbl();
        JPanel varPnl = setVarPnl();
        setUpTaskList();
        JPanel tasksPnl = setTaskPnl();
        setUpAddBtn();
        JPanel addPanel = setAddPnl();
        setIcon();

        this.setTitle("Simple To Do");
        this.setSize(X_VALUE, Y_VALUE);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.GREEN);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(tabPnl);
        this.add(varPnl);
        this.add(tasksPnl);
        this.add(addPanel);

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        ResetService resetService = new ResetService(config.getResetHour(), config.getResetMin(), this);
        resetService.init();
    }

    private void setIcon() {
        URL url = TasksFrame.class.getResource("/imgs/icons8-clock-icon.png");
        assert url != null;
        ImageIcon icon = new ImageIcon(url);
        this.setIconImage(icon.getImage());
    }

    private void setUpConfBtn() {

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/conf.png")));
        Image img = icon.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);

        confBtn = new JButton(new ImageIcon(img));
        confBtn.setBorderPainted(false);
        confBtn.setContentAreaFilled(false);
        confBtn.setFocusPainted(false);
        confBtn.setOpaque(false);

        confBtn.addActionListener(e -> {
            if(confFrame == null)
                confFrame = new ConfFrame(this);
            else if (!confFrame.isVisible())
                confFrame.setVisible(true);
            else
                confFrame.toFront();
        });
        confBtn.setBounds(215, -2, 35, 30);
        confBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                confBtn.setBounds(214, -3, 35, 30);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                confBtn.setBounds(215, -2, 35, 30);
            }
        });
    }

    private JPanel setAddPnl() {
        JPanel addPanel = new JPanel();
        addPanel.setBackground(TokyoNightColors.BG);
        addPanel.setBounds(0, 395, 350, 30);
        addPanel.setLayout(null);
        addPanel.add(addBtn);
        return addPanel;
    }

    private void setUpAddBtn() {

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/add.png")));
        Image img = icon.getImage().getScaledInstance(53, 53, Image.SCALE_SMOOTH);

        addBtn = new JButton(new ImageIcon(img));
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(false);
        addBtn.addActionListener(e -> {
            if(e.getSource() == addBtn){
                if(addFrame == null)
                    addFrame = new AddTaskFrame(this);
                else if (!addFrame.isVisible())
                    addFrame.setVisible(true);
                else
                    addFrame.toFront();
            }
        });
        addBtn.setBounds(280, -19, 58, 58);
    }

    private JPanel setTaskPnl() {
        JScrollPane scrollPane = new JScrollPane(tasksList);
        scrollPane.setBounds(5, 10, 320, 325);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, TokyoNightColors.GRID));
        scrollPane.getViewport().setBackground(TokyoNightColors.BG);
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) scrollPane.getViewport().getView();
                table.clearSelection();
            }
        });

        JPanel tasksPnl = new JPanel();
        tasksPnl.setBackground(TokyoNightColors.BG);
        tasksPnl.setBounds(0,50, 350, 345);
        tasksPnl.setLayout(null);
        tasksPnl.add(scrollPane);
        return tasksPnl;
    }

    private void applyTokyoNightStyle(JTable table) {

        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        table.setBackground(TokyoNightColors.BG);
        table.setForeground(TokyoNightColors.FG);
        table.setFont(new Font("Segue UI", Font.PLAIN, 14));

        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(TokyoNightColors.GRID);

        table.setSelectionBackground(table.getBackground());
        table.setSelectionForeground(table.getForeground());

        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(TokyoNightColors.HEADER);
        header.setForeground(TokyoNightColors.FG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createMatteBorder(1,1,1,1, TokyoNightColors.FG));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                boolean isEven = row % 2 == 0;

                setBackground(isEven ? TokyoNightColors.BG : TokyoNightColors.BG_ALT);
                setForeground(TokyoNightColors.FG);

                if (isSelected) {
                    setBorder(BorderFactory.createMatteBorder(
                            2, 0, 2, 0, TokyoNightColors.ACCENT));
                } else {
                    setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, TokyoNightColors.GRID));
                }
                return c;
            }
        });

        // Custom CheckBox
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                boolean isEven = row % 2 == 0;

                setBackground(isEven ? TokyoNightColors.BG : TokyoNightColors.BG_ALT);
                setForeground(TokyoNightColors.FG);

                if (value instanceof Boolean) {
                    boolean val = (Boolean) value;
                    setText(val ? "Done" : "To Do");
                    setHorizontalAlignment(CENTER);
                    setForeground(val ? TokyoNightColors.GREEN : TokyoNightColors.RED);
                    setFont(new Font("Segoe UI", Font.BOLD, 15));
                }

                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                            ? TokyoNightColors.BG
                            : TokyoNightColors.BG_ALT);
                }

                if (isSelected) {
                    setBorder(BorderFactory.createMatteBorder(
                            2, 0, 2, 0, TokyoNightColors.ACCENT));
                } else {
                    setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, TokyoNightColors.GRID));
                }

                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if(row == -1 || col != 1)
                    return;

                boolean current = (boolean) table.getValueAt(row, col);
                table.setValueAt(!current, row, col);
            }
        });
    }

    private void setUpTaskList() {
        model = new CustomTaskTable(taskService.getTasks());

        tasksList = new JTable(model);
        tasksList.getColumnModel().getColumn(0).setPreferredWidth(225);
        tasksList.getColumnModel().getColumn(0).setResizable(false);
        tasksList.getColumnModel().getColumn(1).setPreferredWidth(75);
        tasksList.getColumnModel().getColumn(1).setResizable(false);

        tasksList.setBounds(5, 5, 300, 500);
        tasksList.setBorder(BorderFactory.createMatteBorder(0,0,0, 0,Color.BLACK));
        tasksList.setFont(new Font("Roboto", Font.PLAIN, 16));

        JPopupMenu popupMenu = getJPopupMenu();

        tasksList.setComponentPopupMenu(popupMenu);
        tasksList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tasksList.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < tasksList.getRowCount()) {
                        tasksList.setRowSelectionInterval(row, row);
                    }
                }
            }
        });

        applyTokyoNightStyle(tasksList);
    }

    private JPopupMenu getJPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem item = new JMenuItem("Delete");
        item.addActionListener(e -> {
            taskService.deleteTask(model.getTasks().get(tasksList.getSelectedRow()));
            refreshList();
        });
        item.setBackground(TokyoNightColors.BG);
        item.setForeground(TokyoNightColors.RED_ALT);
        item.setBorderPainted(false);
        item.setFocusPainted(false);
        item.setFont(new Font("Roboto", Font.BOLD, 14));

        popupMenu.add(item);
        popupMenu.setBorder(BorderFactory.createMatteBorder(1,1,1,1, TokyoNightColors.GRID));
        return popupMenu;
    }

    private JPanel setVarPnl() {
        JPopupMenu popUpMenuClock = setJPopUpMenu();
        setUpClockLbl(popUpMenuClock);
        JPanel varPnl = new JPanel();
        varPnl.setBackground(TokyoNightColors.HEADER);
        varPnl.setBounds(0,23, 350, 28);
        varPnl.setLayout(null);
        varPnl.add(titleLbl);
        varPnl.add(clockLbl);
        return varPnl;
    }

    private void setUpClockLbl(JPopupMenu popUpMenuClock) {
        clockLbl.setFont(new Font("Roboto", Font.PLAIN, 16));
        clockLbl.setForeground(Color.white);
        clockLbl.setBounds(265,8,100,16);
        clockLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    resumePauseTimer();
            }
        });
        clockLbl.setComponentPopupMenu(popUpMenuClock);
        clockLbl.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e){
                if(!timer.isRunning())
                    clockLbl.setFont(new Font("Roboto", Font.BOLD, 16));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!timer.isRunning())
                    clockLbl.setFont(new Font("Roboto", Font.PLAIN, 16));
            }
        });
    }

    private JPopupMenu setJPopUpMenu() {
        JPopupMenu popUpMenuClock = new JPopupMenu();
        popUpMenuClock.setFocusable(false);

        JMenuItem itemForClock = new JMenuItem("Reset");
        itemForClock.addActionListener(ae -> {
            resetTimer();
            clockLbl.setText(formatTime(config.getTimerHour(), config.getTimerMin(), 0));
            stopTimer();
        });
        itemForClock.setBorderPainted(false);
        itemForClock.setFocusPainted(false);
        itemForClock.setBackground(TokyoNightColors.BG);
        itemForClock.setForeground(TokyoNightColors.ACCENT);

        popUpMenuClock.add(itemForClock);
        popUpMenuClock.setBorder(BorderFactory.createMatteBorder(1,1,1,1,TokyoNightColors.GRID));
        return popUpMenuClock;
    }

    private static JLabel setTitleLbl() {
        JLabel titleLbl = new JLabel("<html>Daily List</html>");
        titleLbl.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLbl.setForeground(TokyoNightColors.FG);
        titleLbl.setBounds(5,8,75,16);
        return titleLbl;
    }

    public void refreshList(){
        model.setTasks(taskService.getTasks());
        model.refresh();
    }

    private String formatTime(int h, int m, int s) {
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private Timer createTimer(){
        return new javax.swing.Timer(1000, e -> {
            if (sec == 0) {
                if (min == 0) {
                    if (hour == 0) {
                        ((javax.swing.Timer)e.getSource()).stop();
                        makeSound();
                        return;
                    } else {
                        hour--;
                        min = 59;
                        sec = 59;
                    }
                } else {
                    min--;
                    sec = 59;
                }
            } else {
                sec--;
            }

            clockLbl.setText(formatTime(hour, min, sec));
        });
    }

    private void makeSound() {
        SoundMaker.getInstance().makeSound("/sound/PS2_TIMER_SOUND.wav");
    }


    private void resumePauseTimer(){
        if(timer.isRunning())
            stopTimer();
        else
            if(hour == null || min == null || sec == null)
                startTimer();
            else
                resumeTimer();
    }

    private void resetTimer(){
        hour = config.getTimerHour();
        min = config.getTimerMin();
        sec = 0;
    }

    public void updateTimer(){
        if (timer.isRunning())
            stopTimer();
        resetTimer();
        clockLbl.setText(formatTime(hour,min,sec));
    }

    private void startTimer(){
        resetTimer();
        timer.start();
        clockLbl.setFont(new Font("Roboto", Font.BOLD, 16));
    }

    private void stopTimer(){
        timer.stop();
        clockLbl.setFont(new Font("Roboto", Font.PLAIN, 16));
    }

    private void resumeTimer(){
        timer.start();
    }
}
