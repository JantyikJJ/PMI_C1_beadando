package com.exmodify.healtrecords.gui.dialogs;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.gui.BaseGUI;
import com.exmodify.healtrecords.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tips extends BaseGUI {

    final String[] tips;
    int currentTip;
    private JPanel mainPanel;
    private JButton next;
    private JButton close;
    private JButton noMoreTips;
    private JLabel counter;
    private JLabel tip;

    public Tips() {
        tips = new String[] {
                "<html>Auto saving is turned on by default!</html>",
                "<html>You can generate test data with <br>File -> Generate test data!</html>"
        };
        currentTip = Main.getRandom().nextInt(tips.length);
        tip.setText(tips[currentTip]);
        counter.setText((currentTip + 1) + "/" + tips.length);

        next.addActionListener(this::nextClicked);
        close.addActionListener(this::closeClicked);
        noMoreTips.addActionListener(this::noMoreTipsClicked);
    }

    @Override
    protected void initFrame() {
        frame = new JFrame("Tips");
        frame.setContentPane(mainPanel);
        frame.setMinimumSize(new Dimension(400, 130));
        centerPosition(mainPanel.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame.setResizable(false);
        frame.pack();

    }

    private void nextClicked(ActionEvent e) {
        currentTip++;
        if (currentTip == tips.length) {
            currentTip = 0;
        }
        tip.setText(tips[currentTip]);
        counter.setText((currentTip + 1) + "/" + tips.length);
        //frame.setSize(tip.getWidth() + mainPanel.getInsets().left + mainPanel.getInsets().right, frame.getHeight());
    }
    private void closeClicked(ActionEvent e) {
        frame.setVisible(false);
    }
    private void noMoreTipsClicked(ActionEvent e) {
        Config.showTips = false;
        try {
            Config.save();
        }
        catch (Exception ignored) { }
        frame.setVisible(false);
    }
}
