package com.exmodify.healtrecords.gui;

import javax.swing.*;

public abstract class BaseGUI {
    protected JFrame frame;

    protected abstract void initFrame();

    public void show() {
        initFrame();
        frame.setVisible(true);
    }
}
