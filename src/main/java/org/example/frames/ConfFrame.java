package org.example.frames;

import org.example.entity.Config;
import org.example.service.PersistService;
import org.example.util.ScreenSize;
import org.example.util.TokyoNightColors;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfFrame extends JFrame{

    private JLabel minLbl, hourLbl, resetTimeLbl;
    private JTextField minTxt, hourTxt, resetTimeTxt;
    private int resHour = 6, resMin = 0;
    private JButton saveBtn, addBtn, subBtn;
    private JPanel tabPnl;
    private final Timer sumHoldTimer = getSumTimer();
    private final Timer subHoldTimer = getSubTimer();
    private final TasksFrame tasksFrame;
    private final PersistService persistService = PersistService.getInstance();

    private static final int X_VALUE = 350, Y_VALUE = 115;

    public ConfFrame(TasksFrame tasksFrame){

        this.tasksFrame = tasksFrame;
        setUpLabels();
        setUpTexts();
        setUpAddBtn();
        setUpSubBtn();
        setUpSaveBtn();
        setUpTxtValues();

        this.setUndecorated(true);
        this.setTitle("Simple To Do");
        this.setSize(X_VALUE, Y_VALUE);
        this.setResizable(false);
        this.setLayout(null);
        this.getContentPane().setBackground(TokyoNightColors.BG);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.add(tabPnl);
        this.add(minLbl);
        this.add(hourLbl);
        this.add(resetTimeLbl);

        this.add(minTxt);
        this.add(hourTxt);
        this.add(resetTimeTxt);

        this.add(addBtn);
        this.add(subBtn);
        this.add(saveBtn);

        int x = (int) (ScreenSize.WIDTH * 0.23);
        int y = (int) (ScreenSize.HEIGHT * 0.292);
        this.setLocation(x, y);

        this.setVisible(true);
    }

    private void setUpTxtValues() {
        this.hourTxt.setText(String.valueOf(tasksFrame.getConfig().getTimerHour()));
        this.minTxt.setText(String.valueOf(tasksFrame.getConfig().getTimerMin()));

        this.resHour = tasksFrame.getConfig().getResetHour();
        this.resMin = tasksFrame.getConfig().getResetMin();
        resetTimeTxt.setText(formatTime(resHour, resMin));
    }

    private void setUpSaveBtn() {
        saveBtn = new JButton("<html>Save</html>");
        saveBtn.setBounds(245, 80,  70, 25);
        saveBtn.setFont(new Font("Roboto", Font.BOLD, 15));
        saveBtn.setFocusPainted(false);
        saveBtn.setBackground(TokyoNightColors.GREEN);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(new MatteBorder(1,1,1,1,TokyoNightColors.FG));
        saveBtn.addActionListener(e -> {
            if (!hourTxt.getText().isEmpty() && !minTxt.getText().isEmpty()){
                Config newConf = new Config(Integer.parseInt(hourTxt.getText()),
                        Integer.parseInt(minTxt.getText()),
                        resHour,
                        resMin);
                persistService.saveConf(newConf);
                tasksFrame.setConfig(newConf);
                tasksFrame.updateTimer();
            }
        });

        saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveBtn.setBackground(TokyoNightColors.DARK_GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveBtn.setBackground(TokyoNightColors.GREEN);
                saveBtn.setForeground(Color.WHITE);
            }
        });
    }

    private void setUpSubBtn() {
        subBtn = initBtn("-", 225, 40);

        subBtn.addActionListener(e -> {
           subResetTimer();
        });
        subBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                subHoldTimer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                subHoldTimer.stop();
            }
        });
    }

    private void setUpAddBtn() {
        addBtn = initBtn("+", 312, 40);
        addBtn.addActionListener(e -> {
            sumResetTime();
        });

        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sumHoldTimer.start();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                sumHoldTimer.stop();
            }
        });
    }

    private void subResetTimer() {
        resMin -= 15;
        if(resMin == -15){
            resMin = 45;
            resHour --;
            if(resHour == -1)
                resHour = 23;
        }
        resetTimeTxt.setText(formatTime(resHour, resMin));
    }

    private void sumResetTime() {
        resMin += 15;
        if (resMin == 60){
            resMin = 0;
            resHour ++;
            if (resHour == 24)
                resHour = 0;
        }
        resetTimeTxt.setText(formatTime(resHour, resMin));
    }

    private JButton initBtn(String txt, int x, int y){
        JButton btn = new JButton("<html>"+txt+"</html>");
        btn.setBounds(x,y,25,25);
        btn.setBackground(TokyoNightColors.BG);
        btn.setFont(new Font("Roboto", Font.BOLD, 18));
        btn.setForeground(TokyoNightColors.FG);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0,0,0,3));

        return btn;
    }

    private void setUpTexts() {
        hourTxt = initTxt(70, 40);

        minTxt = initTxt(70, 80);

        resetTimeTxt = initTxt(255, 40);
        resetTimeTxt.setEnabled(false);
    }

    private JTextField initTxt(int x, int y){
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 50, 25);
        txt.setBackground(TokyoNightColors.BG_ALT);
        txt.setForeground(TokyoNightColors.FG);
        txt.setFont(new Font("Roboto", Font.BOLD, 15));
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                    return;
                }
                if (txt.getText().length() >= 2)
                    e.consume();

                if(!txt.getText().isEmpty())
                    if(Integer.parseInt(txt.getText() + c) > 59)
                        e.consume();
            }
        });
        return txt;
    }

    private void setUpLabels() {
        tabPnl = new TabPnl(this, X_VALUE);
        hourLbl = initLbl("Hours: ", 10, 40);
        minLbl = initLbl("Minutes: ", 10, 80);
        resetTimeLbl = initLbl("Reset Time: ", 140, 40);
    }

    private JLabel initLbl(String txt, int x, int y){
        JLabel jLabel = new JLabel(txt);
        jLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        jLabel.setForeground(TokyoNightColors.FG);
        jLabel.setBounds(x,y, 80, 25);
        return jLabel;
    }

    private String formatTime(int h, int m) {
        return String.format("%02d:%02d", h, m);
    }

    private Timer getSubTimer() {
        return new Timer(100, e -> {
            subResetTimer();
        });
    }

    private Timer getSumTimer() {
        return new Timer(100, e -> {
            sumResetTime();
        });
    }
}
