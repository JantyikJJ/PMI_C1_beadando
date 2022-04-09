package com.exmodify.healtrecords.gui;

import javax.swing.*;

public class Entries extends BaseGUI {
    private JButton button1;
    private JPanel panel1;


    @Override
    protected void initFrame() {
        frame = new JFrame("Splash");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
}
