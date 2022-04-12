package com.exmodify.healtrecords.gui.components;

import javax.swing.*;
import java.awt.*;

public class JImagePanel extends JPanel {

    private transient Icon icon;

    /**
     * JPanel supporting image background.
     */
    public JImagePanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // if icon is set -> paint it
        if (icon != null) {
            icon.paintIcon(this, g, 0, 0);
        }
    }

    /**
     * Set Icon that automatically invokes repainting
     * @param newIcon the new icon
     */
    public void setIcon(Icon newIcon) {
        if (newIcon != icon) {
            icon = newIcon;
            repaint();
        }
    }
}
