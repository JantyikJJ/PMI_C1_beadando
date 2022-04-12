package com.exmodify.healtrecords.gui;

import javax.swing.*;
import java.awt.*;

public abstract class BaseGUI {
    protected JFrame frame;

    protected abstract void initFrame();

    /**
     * Shows the JFrame after initialisation
     */
    public void show() {
        initFrame();
        frame.setVisible(true);
    }

    /**
     * Calculates then sets the frame to the center of the screen
     * @param preferredSize the size of the main panel of the frame
     */
    protected void centerPosition(Dimension preferredSize) {
        // get screen size
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // calculate top left corner's position
        int x = (dim.width - preferredSize.width) / 2;
        int y = (dim.height - preferredSize.height) / 2;
        frame.setLocation(x, y);
    }
}
