package com.exmodify.healtrecords.gui;

import javax.swing.*;
import java.awt.*;

public abstract class BaseGUI {
    protected JFrame frame;

    protected abstract void initFrame();

    public void show() {
        initFrame();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    protected void centerPosition(Dimension preferredSize) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (dim.width - preferredSize.width) / 2;
        int y = (dim.height - preferredSize.height) / 2;
        frame.setLocation(x, y);
    }
}
