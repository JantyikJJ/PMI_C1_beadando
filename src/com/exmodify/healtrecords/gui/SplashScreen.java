package com.exmodify.healtrecords.gui;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.models.events.ProgressChangeListener;
import com.exmodify.healtrecords.gui.components.JImagePanel;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends BaseGUI {
    private final JImagePanel mainPanel;
    private final JProgressBar progressBar;
    private final JLabel progressLabel;

    public SplashScreen() {
        mainPanel = new JImagePanel();
        mainPanel.setLayout(null);
        int splashWidth = 500, splashHeight = 250;
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("splash.png");

            if (is != null) {
                BufferedImage img = ImageIO.read(is);

                splashWidth = img.getWidth();
                splashHeight = img.getHeight();

                mainPanel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
                mainPanel.setMinimumSize(mainPanel.getPreferredSize());
                mainPanel.setIcon(new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        g.drawImage(img, x, y, c);
                    }

                    @Override
                    public int getIconWidth() {
                        return img.getWidth();
                    }

                    @Override
                    public int getIconHeight() {
                        return img.getHeight();
                    }
                });

                is.close();
            }
            else {
                System.err.println("Splash screen image couldn't be loaded from resources.");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        progressBar = new JProgressBar();
        progressLabel = new JLabel("Loading...", SwingConstants.LEFT);

        progressBar.setBounds(0, splashHeight - 25, splashWidth - 150, 25);
        progressBar.setStringPainted(true);
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(new Color(8, 0, 84));

        progressLabel.setBounds(splashWidth - 150, splashHeight - 25, 150, 25);
        progressLabel.setForeground(Color.white);
    }


    @Override
    protected void initFrame() {
        mainPanel.add(progressBar);
        mainPanel.add(progressLabel);

        frame = new JFrame("Splash");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension preferredSize = mainPanel.getPreferredSize();

        int x = (dim.width - preferredSize.width) / 2;
        int y = (dim.height - preferredSize.height) / 2;
        frame.setLocation(x, y);
        frame.setUndecorated(true);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    @Override
    public void show() {
        super.show();

        SwingUtilities.invokeLater(this::load);
    }

    public void load() {
        try {
            progressLabel.setText("Loading config...");
            Config.load();
            // it takes a few milliseconds, but to show a nicer splash screen -> fake loading
            Thread fakeLoad = new Thread(() -> {
                int current = 0;
                while (current < 11) {
                    int finalCurrent = current;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(finalCurrent);
                    });
                    current++;
                    try {
                        Thread.sleep(10);
                    }
                    catch (Exception e) { e.printStackTrace(); }
                }

                loadRecords();
            });
            fakeLoad.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadRecords() {
        progressLabel.setText("Loading records...");
        int next = 85;
        Records.addEventListener((processed, max) -> {
            if (max != 0) {
                progressBar.setValue((int)(10 + next * (processed / (double)max))); // between 10 - 95

                if (processed == max) {
                    progressLabel.setText("Preparing main window...");
                    prepareMain();
                }
            }
            else {
                progressBar.setValue(10 + next);

                progressLabel.setText("Preparing main window...");
                prepareMain();
            }
        });

        Thread th = new Thread(() -> {
            try {
                Records.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        th.start();
    }
    private void prepareMain() {

    }
}
