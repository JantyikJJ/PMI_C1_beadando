package com.exmodify.healtrecords.gui;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.RecordsGenerator;
import com.exmodify.healtrecords.gui.components.RecordsTableModel;
import com.exmodify.healtrecords.gui.dialogs.Edit;
import com.exmodify.healtrecords.main.Main;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;

/**
 * Main GUI of Health Records application.
 */
public class Entries extends BaseGUI {
    /**
     * Main panel containing all the components
     */
    private JPanel mainPanel;
    /**
     * Entries table containing all the Records
     */
    private JTable entries;
    /**
     * Menu bar
     */
    private JMenuBar menuBar;

    /**
     * Edit menu item which can be enabled - disabled based on row selection
     */
    private JMenuItem editItem;
    /**
     * Delete menu item which can be enabled - disabled based on row selection
     */
    private JMenuItem deleteItem;

    /**
     * Default constructor of Entries GUI
     */
    public Entries() {

    }

    /**
     * Builds the menu bar, then creates the table model for the Record entries.
     */
    public void processEntries() {
        buildMenu();

        RecordsTableModel model = new RecordsTableModel();
        entries.setModel(model);
        entries.setAutoCreateColumnsFromModel(true);
    }
    /**
     * Building the menu bar, menus and menu items
     * Attaching appropriate listeners to them
     */
    private void buildMenu() {
        /*  -----------
            FILE
            ----------- */
        JMenu menu = new JMenu("File");

        // File -> Tips: Shows Tips dialogue
        JMenuItem item = new JMenuItem(new AbstractAction("Tips") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTips();
            }
        });
        menu.add(item);

        // File -> Save: Saves all
        item = new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Records.save();
                }
                catch (Exception ignored) { }
            }
        });
        // Setting CTRL + S as shortcut for Save
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menu.add(item);

        // File -> Clear all data: deletes all data from Records database
        item = new JMenuItem(new AbstractAction("Clear all data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Asking the user if they really want to remove all entries for extra security
                int result = JOptionPane.showConfirmDialog(frame, """
                                Are you sure you want to remove all data?
                                
                                This action is irreversible!""",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    // clearing the list
                    Records.getRecords().clear();

                    // auto saving to file or letting the user save whenever they want
                    // based on configuration
                    if (Config.autoSave) {
                        try {
                            Records.save();
                        }
                        catch (Exception ignored) { }
                    }
                    else {
                        Main.setPendingChanges(true);
                    }

                    repaintTable();
                }
            }
        });
        menu.add(item);

        // File -> Generate test data
        item = new JMenuItem(new AbstractAction("Generate test data") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Getting data between 1 - unlimited
                // 0 or closing -> cancel generation
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

                if (input > 0) {
                    // valid input, bigger than 0 -> generate
                    RecordsGenerator.generate(input, Config.autoSave);
                    // if it wasn't saved, save that there are pending changes
                    if (!Config.autoSave) {
                        Main.setPendingChanges(true);
                    }
                    repaintTable();
                }
            }
        });
        menu.add(item);

        // File -> Exit
        item = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // simply set visibility to false -> trigger window on close event
                frame.setVisible(false);
            }
        });
        menu.add(item);

        menuBar.add(menu);

        /*  -----------
            QUICK OPTIONS
            ----------- */
        menu = new JMenu("Quick options");

        // creating checkbox item
        JCheckBoxMenuItem checkboxItem = new JCheckBoxMenuItem("Auto save after row edit");
        // setting the default value from the configuration
        checkboxItem.setState(Config.autoSave);
        // attacking change listener
        checkboxItem.addActionListener((e) -> {
            // getting the source, then the value
            AbstractButton ab = (AbstractButton)e.getSource();
            Config.autoSave = ab.getModel().isSelected();

            // saving config to file
            try {
                Config.save();
            }
            catch (Exception ignored) { }
        });
        menu.add(checkboxItem);

        // Same structure as auto save
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

        // Same structure as auto save
        checkboxItem = new JCheckBoxMenuItem("Reverse naming order");
        checkboxItem.setState(Config.reverseNaming);
        checkboxItem.addActionListener((e) -> {
            AbstractButton ab = (AbstractButton)e.getSource();
            Config.reverseNaming = ab.getModel().isSelected();
            try {
                Config.save();
            }
            catch (Exception ignored) { }
            // repainting table after naming order change
            repaintTable();
        });
        menu.add(checkboxItem);

        menuBar.add(menu);

        // Adding plain menu item to menu bar instead of into a menu for ease of access
        item = new JMenuItem(new AbstractAction("Add") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Edit dialogue can be used without reference record to create a new record
                Edit add = new Edit();
                add.show(null);
            }
        });
        menuBar.add(item);

        // same logic as "Add" menu item
        item = new JMenuItem(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Edit edit = new Edit();
                edit.show(Records.getRecords().get(entries.getSelectedRow()));
            }
        });
        // saving "Edit" menu item as global property for enabling - disabling based on row selection
        editItem = item;
        item.setEnabled(false);
        menuBar.add(item);

        // same logic as "Edit"
        item = new JMenuItem(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = entries.getSelectedRow();
                Records.getRecords().remove(entries.getSelectedRow());
                if (Config.autoSave) {
                    try {
                        Records.save();
                    }
                    catch (Exception ignored) { }
                }
                else {
                    Main.setPendingChanges(true);
                }
                repaintTable();

                SwingUtilities.invokeLater(() -> {
                    if (entries.getRowCount() != 0) {
                        int newIndex = index;
                        if (newIndex == entries.getRowCount()) {
                            newIndex--;
                        }
                        entries.setRowSelectionInterval(newIndex, newIndex);
                    }
                });
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteItem = item;
        item.setEnabled(false);
        menuBar.add(item);

        // if row selected -> allow row delete / edit
        // if not -> disable row delete / edit
        entries.getSelectionModel().addListSelectionListener(e -> {
            if (entries.getSelectedRow() > -1) {
                editItem.setEnabled(true);
                deleteItem.setEnabled(true);
            }
            else {
                editItem.setEnabled(false);
                deleteItem.setEnabled(false);
            }

        });

        // double click event listener for row edit
        entries.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                // if double click AND there's a row selected
                if (mouseEvent.getClickCount() == 2 && entries.getSelectedRow() != -1) {
                    Edit edit = new Edit();
                    edit.show(Records.getRecords().get(entries.getSelectedRow()));
                }
            }
        });
    }

    /**
     * Initializes the main frame for the GUI.
     * It also sets the GUI to the center of the screen
     *
     */
    @Override
    protected void initFrame() {
        frame = new JFrame("Health Records");
        frame.setContentPane(mainPanel);
        // center frame
        centerPosition(mainPanel.getPreferredSize());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // if there's unsaved changes
                if (Main.isPendingChanges()) {
                    // show a question dialogue if the user would like to save changes
                    int result = JOptionPane.showConfirmDialog(frame, """
                                    You have pending changes in the records!
                                    
                                    Would you like to save before quitting?""", "Question",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    // if yes -> save
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            Records.save();
                        }
                        catch (Exception ignored) { }
                    }
                }
                // close frame and exit
                frame.setVisible(false);
                System.exit(0);
                super.windowClosing(e);
            }
        });
        // don't do anything on close -> trigger
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
    }

    /**
     * Shows the main form, and the Tips dialogue if showing tips on startup is enabled.
     */
    @Override
    public void show() {
        super.show();

        if (Config.showTips) {
            showTips();
        }
    }

    /**
     * Shows the Tips dialogue for quick tips!
     * It's on a different thread so the dialogue isn't waited to be closed
     */
    private void showTips() {
        Thread th = new Thread(() -> Main.getTips().show());
        th.start();
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

    /**
     * Repaints the table, used after the Records database have been modified.
     */
    public void repaintTable() {
        ((AbstractTableModel) entries.getModel()).fireTableChanged(new TableModelEvent(entries.getModel()));
    }
}
