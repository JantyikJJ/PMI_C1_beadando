package com.exmodify.healtrecords.gui.dialogs;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.gui.BaseGUI;
import com.exmodify.healtrecords.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Quick Tips GUI
 */
public class Tips extends BaseGUI {
    /**
     * Constant String array
     */
    final String[] tips;
    /**
     * The index of the currently shown tip
     */
    int currentTip;
    /**
     * The main panel containing all the components
     */
    private JPanel mainPanel;
    /**
     * The next tip button
     */
    private JButton next;
    /**
     * The close Tips GUI button
     */
    private JButton close;
    /**
     * The No more tips button
     */
    private JButton noMoreTips;
    /**
     * The counter label showing the current / total tips
     */
    private JLabel counter;
    /**
     * The current tip label
     */
    private JLabel tip;

    /**
     * Creates a Tips dialogue showing quick tips!
     */
    public Tips() {
        // generate tips constant and set first text
        tips = new String[] {
                "<html>Auto saving is turned on by default!</html>",
                "<html>You can generate test data with <br>File -> Generate test data!</html>",
                "<html>Double clicking on a row automatically opens up the edit window.</html>",
                "<html>Data validation is in early stage. If you input incorrect data, it won't be saved, " +
                        "and you will be notified, but it won't show where the error is.</html>"
        };
        currentTip = Main.getRandom().nextInt(tips.length);
        tip.setText(tips[currentTip]);
        counter.setText((currentTip + 1) + "/" + tips.length);

        // attach event listeners
        next.addActionListener(this::nextClicked);
        close.addActionListener(this::closeClicked);
        noMoreTips.addActionListener(this::noMoreTipsClicked);
    }

    /**
     * Initializes the JFrame
     */
    @Override
    protected void initFrame() {
        frame = new JFrame("Tips");
        frame.setContentPane(mainPanel);
        // settings minimum size for the frame for the longest text
        // for some reason the frame doesn't get auto resized based on the content
        frame.setMinimumSize(new Dimension(400, 160));
        centerPosition(mainPanel.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();

    }

    /**
     * Next button event listener
     * @param e the event details
     */
    private void nextClicked(ActionEvent e) {
        // increasing counter -> reset if going bast last item
        currentTip++;
        if (currentTip == tips.length) {
            currentTip = 0;
        }
        // updating text
        tip.setText(tips[currentTip]);
        counter.setText((currentTip + 1) + "/" + tips.length);
        //frame.setSize(tip.getWidth() + mainPanel.getInsets().left + mainPanel.getInsets().right, frame.getHeight());
    }

    /**
     * Close button event listener
     * @param e the event details
     */
    private void closeClicked(ActionEvent e) {
        frame.setVisible(false);
    }

    /**
     * No more tips button event listener
     * @param e the event details
     */
    private void noMoreTipsClicked(ActionEvent e) {
        // disabling quick tip appearance on main form load
        Config.showTips = false;
        try {
            Config.save();
        }
        catch (Exception ignored) { }
        frame.setVisible(false);
    }
}
