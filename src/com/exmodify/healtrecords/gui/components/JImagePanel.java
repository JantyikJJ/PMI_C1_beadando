package com.exmodify.healtrecords.gui.components;

import javax.swing.*;
import java.awt.*;

public class JImagePanel extends JPanel {

    private transient Icon icon;

    public JImagePanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            icon.paintIcon(this, g, 0, 0);
        }
    }

    public void setIcon(Icon newIcon) {
        if (newIcon != icon) {
            icon = newIcon;
            repaint();
        }
    }
    public Icon getIcon() {
        return icon;
    }
}
