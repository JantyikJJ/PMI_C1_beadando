package com.exmodify.healtrecords.gui;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.models.events.ProgressChangeListener;
import com.exmodify.healtrecords.gui.components.JImagePanel;
import com.exmodify.healtrecords.gui.dialogs.Tips;
import com.exmodify.healtrecords.main.Main;
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

    /**
     * Creates a splash screen instance that loads the configuration and records
     * After loading it shows the main, "Entries" form.
     */
    public SplashScreen() {
        // custom image panel initialization
        // JPanel doesn't support background image
        // GUI edit doesn't allow me to use this panel
        mainPanel = new JImagePanel();
        // null -> manual layout
        mainPanel.setLayout(null);

        int splashWidth = 500, splashHeight = 250;
        try {
            // Reading splash image from resources
            InputStream is = getClass().getClassLoader().getResourceAsStream("splash.png");

            if (is != null) {
                BufferedImage img = ImageIO.read(is);

                splashWidth = img.getWidth();
                splashHeight = img.getHeight();

                // setting panel size to be identical to the image size
                mainPanel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
                mainPanel.setMinimumSize(mainPanel.getPreferredSize());
                // creating icon from this buffered image
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

        // setting some variables for easeier change
        int height = 25;
        int labelWidth = 200;

        // calculating size and position for the for progress bar
        progressBar.setBounds(0, splashHeight - height, splashWidth - labelWidth, height);
        // enable text display that draws x% over the progres sbar
        progressBar.setStringPainted(true);
        // setting bounds
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(new Color(8, 0, 84));

        // calculating size and posizion for the label
        progressLabel.setBounds(splashWidth - labelWidth, splashHeight - height, labelWidth, height);
        progressLabel.setForeground(Color.white);
    }


    @Override
    protected void initFrame() {
        mainPanel.add(progressBar);
        mainPanel.add(progressLabel);

        frame = new JFrame("Splash");

        centerPosition(mainPanel.getPreferredSize());

        // splash screens don't have borders the buttons, so removing that
        frame.setUndecorated(true);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    @Override
    public void show() {
        super.show();
        // initiating load on a different thread
        SwingUtilities.invokeLater(this::load);
    }

    public void load() {
        try {
            progressLabel.setText("Loading config...");
            Config.load();
            // it takes a few milliseconds, but to show a nicer splash screen -> fake loading
            Thread fakeLoad = new Thread(() -> {
                int current = 0;
                while (current < 6) {
                    // since it's running in runnable
                    // the variable has to be saved where it's particularly final
                    int finalCurrent = current;
                    SwingUtilities.invokeLater(() -> {
                        // setting it on a different thread so the visuals are updated properly
                        progressBar.setValue(finalCurrent);
                    });
                    current++;
                    try {
                        // waiting 5ms so it doesn't instantly fake load from 1 to 5
                        Thread.sleep(5);
                    }
                    catch (Exception e) { e.printStackTrace(); }
                }
                // loading records
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
        int next = 95;
        // Records event listener -> if a new record is processed and parsed, this gets fired
        Records.addEventListener((processed, max) -> {
            // if max != 0 -> there are items in XML
            // if it is 0, then there aren't any files
            if (max != 0) {
                // calculating progress from current / max and multiplying it with maximum progress of this stage
                // starting from the max of the previous stage (config loading is from 0-5%)
                progressBar.setValue((int)(5 + next * (processed / (double)max))); // between 5 - 100

                // if the last one is processed, show main form
                if (processed == max) {
                    progressLabel.setText("Preparing main window...");
                    prepareMain();
                }
            }
            else {
                progressBar.setValue(5 + next);

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
        Thread th = new Thread(() -> {
            Entries entries = Main.getEntries();
            // generating elements for Entries form, then show it
            entries.processEntries();
            frame.setVisible(false);
            entries.show();
        });
        th.start();
    }
}
