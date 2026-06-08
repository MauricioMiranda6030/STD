package org.example.frames;

import lombok.Data;
import org.example.util.TokyoNightColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TabPnl extends JPanel{

    private int xMouse, yMouse;
    private JButton minBtn, closeBtn;
    private final int xValue;
    private final JFrame frame;

    public TabPnl(JFrame frame, int xValue){
        this.xValue = xValue;
        this.frame = frame;
        setUpMinBtn(frame);
        setUpCloseBtn();
        this.setBounds(0,0, xValue,23);
        this.setBackground(TokyoNightColors.NIGHT);
        this.add(minBtn);
        this.add(closeBtn);
        this.setLayout(null);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                moveWindow(e);
            }
        });
    }

    private void setUpCloseBtn() {
        closeBtn = new JButton("X");
        closeBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBounds(xValue - 45, 0, 45, 23);
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frame instanceof TasksFrame)
                    System.exit(0);
                frame.dispose();
                closeBtn.setBackground(null);
            }
        });
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(Color.red);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(null);
            }
        });
        closeBtn.setBackground(null);
        closeBtn.setBorder(null);
    }

    private void setUpMinBtn(JFrame frame) {
        minBtn = new JButton("_");
        minBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        minBtn.setForeground(Color.WHITE);
        minBtn.setBounds(xValue - 90, 0, 45, 23);
        minBtn.setBackground(null);
        minBtn.setBorder(null);
        minBtn.setFocusPainted(false);
        minBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setState(Frame.ICONIFIED);
            }
        });
        minBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                minBtn.setBackground(TokyoNightColors.ACCENT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minBtn.setBackground(null);
            }
        });
    }

    private void moveWindow(MouseEvent ev){
        int x = ev.getXOnScreen();
        int y = ev.getYOnScreen();
        frame.setLocation(x - xMouse,y - yMouse);
    }
}
