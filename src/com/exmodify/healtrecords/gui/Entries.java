package com.exmodify.healtrecords.gui;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.RecordsGenerator;
import com.exmodify.healtrecords.database.models.Record;
import com.exmodify.healtrecords.database.models.events.ProgressChangeListener;
import com.exmodify.healtrecords.gui.components.RecordsTableModel;
import com.exmodify.healtrecords.main.Main;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Entries extends BaseGUI {
    private JPanel mainPanel;
    private JTable entries;
    private JMenuBar menuBar;
    private final List<ProgressChangeListener> listeners;

    public Entries() {
        listeners = new ArrayList<>();
    }

    public void addProgressListener(ProgressChangeListener l) {
        listeners.add(l);
    }

    public void processEntries() {
        buildMenu();


        List<Record> records = Records.getRecords();
        RecordsTableModel model = new RecordsTableModel();
        entries.setModel(model);
        entries.setAutoCreateColumnsFromModel(true);


        int c = records.size();
        if (c == 0) {
            for (ProgressChangeListener listener : listeners) {
                listener.update(0, 0);
            }
        }
        else {
            for (int i = 0; i < c; i++) {
                for (ProgressChangeListener listener : listeners) {
                    listener.update(i + 1, c);
                }
            }
        }
    }

    private void buildMenu() {
        /*  -----------
            FILE
            ----------- */
        JMenu menu = new JMenu("File");

        JMenuItem item = new JMenuItem(new AbstractAction("Edit selected") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Edit
            }
        });
        item.setEnabled(false);
        menu.add(item);

        item = new JMenuItem(new AbstractAction("Tips") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTips();
            }
        });
        menu.add(item);

        item = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Records.save();
                }
                catch (Exception ignored) { }
            }
        });
        menu.add(item);
        item = new JMenuItem(new AbstractAction("Clear all data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, """
                                Are you sure you want to remove all data?
                                
                                This action is irreversible!""",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    Records.getRecords().clear();
                    try {
                        Records.save();
                    }
                    catch (Exception ignored) { }

                    repaintTable();
                }
            }
        });
        menu.add(item);

        item = new JMenuItem(new AbstractAction("Generate test data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = 0;
                do {
                    if (input == -1) {
                        JOptionPane.showMessageDialog(frame, "Invalid value!");
                    }
                    input = parseInt(JOptionPane.showInputDialog(frame, """
                        How many should be generated?
                        If you leave the input empty, it'll generate 100.
                        If you write 0 it'll cancel the generation.""", "Generate", JOptionPane.QUESTION_MESSAGE));
                }
                while (input == -1);

                System.out.println(input);

                if (input > 0) {
                    RecordsGenerator.generate(input, true);
                    repaintTable();
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("Settings");
        item.addActionListener((e) -> {
            // Open settings panel
        });
        menu.add(item);

        menuBar.add(menu);

        /*  -----------
            QUICK OPTIONS
            ----------- */
        menu = new JMenu("Quick options");

        JCheckBoxMenuItem checkboxItem = new JCheckBoxMenuItem("Auto save after change");
        checkboxItem.setState(Config.autoSave);
        checkboxItem.addActionListener((e) -> {
            AbstractButton ab = (AbstractButton)e.getSource();
            Config.autoSave = ab.getModel().isSelected();
            try {
                Config.save();
            }
            catch (Exception ignored) { }
        });
        menu.add(checkboxItem);

        checkboxItem = new JCheckBoxMenuItem("Show tips on startup");
        checkboxItem.setState(Config.showTips);
        checkboxItem.addActionListener((e) -> {
            AbstractButton ab = (AbstractButton)e.getSource();
            Config.showTips = ab.getModel().isSelected();
            try {
                Config.save();
            }
            catch (Exception ignored) { }
        });
        menu.add(checkboxItem);

        menuBar.add(menu);
    }

    @Override
    protected void initFrame() {
        frame = new JFrame("Health Records");
        frame.setContentPane(mainPanel);

        centerPosition(mainPanel.getPreferredSize());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    @Override
    public void show() {
        super.show();

        if (Config.showTips) {
            showTips();
        }
    }

    private void showTips() {
        Thread th = new Thread(() -> {
            Main.getTips().show();
        });
        th.start();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Parses the String into an Integer with some additional validation
     *
     * @param input the input string
     * @return -1 if the String is not parseable, or it's below 0. Returns the actual number if it's correct.
     */
    private static int parseInt(String input) {
        if (input == null) return 0; // to exit from generation
        if (input.trim().equals("")) return 100; // use default value

        try {
            int value = Integer.parseInt(input);
            if (value < 0) return -1;

            return value;
        }
        catch (Exception ignored) {
            return -1;
        }
    }
    private void repaintTable() {
        ((AbstractTableModel) entries.getModel()).fireTableChanged(new TableModelEvent(entries.getModel()));
    }
}
