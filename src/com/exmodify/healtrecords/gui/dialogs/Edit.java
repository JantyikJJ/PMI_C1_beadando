package com.exmodify.healtrecords.gui.dialogs;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.models.Birth;
import com.exmodify.healtrecords.database.models.Record;
import com.exmodify.healtrecords.main.Main;

/**
 * The Edit / Add GUI
 */
public class Edit extends JDialog {
    /**
     * The main panel containing all the elements
     */
    private JPanel mainPanel;
    /**
     * The OK Button
     */
    private JButton buttonOK;
    /**
     * The cancel button
     */
    private JButton buttonCancel;
    /**
     * First name input
     */
    private JTextField firstName;
    /**
     * Last name input
     */
    private JTextField lastName;
    /**
     * Birthplace input
     */
    private JTextField birthPlace;
    /**
     * Birth year input
     */
    private JTextField birthYear;
    /**
     * Birth month input
     */
    private JTextField birthMonth;
    /**
     * Day of birth input
     */
    private JTextField birthDay;
    /**
     * Gender input
     */
    private JComboBox<String> gender;
    /**
     * Weight input
     */
    private JTextField weight;
    /**
     * Smoker input
     */
    private JCheckBox smoker;
    /**
     * Blood pressure input
     */
    private JComboBox<String> bloodPressure;
    /**
     * Cholesterol input
     */
    private JComboBox<String> cholesterol;

    /**
     * The current Record to be edited. If null -> create new record and add it to list
     */
    private Record currentRecord;

    /**
     * Creates an Edit / Add Record dialog
     */
    public Edit() {
        // default settings for JDialog, so setting what is the main container for the JFrame
        // making it modal and setting default button
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // attaching event listeners
        buttonOK.addActionListener(this::onOK);
        buttonCancel.addActionListener(this::onCancel);

        // doing nothing on close so the
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // disposing dialog on closing
                dispose();
            }
        });
        // attaching Escape button press to close the form
        mainPanel.registerKeyboardAction(this::onCancel, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Editing / creating new Record, then saving it into a list
     * If auto save is enabled, saves into the XML file as well.
     * @param e event details
     */
    private void onOK(ActionEvent e) {
        // if fields aren't valid -> notify user then close without saving
        if (!validateFields() || gender.getSelectedItem() == null || bloodPressure.getSelectedItem() == null
                || cholesterol.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Incorrect data, input won't be saved.");
            dispose();
            return;
        }
        // if currentRecords == null -> adding new item
        if (currentRecord == null) {
            // creating birth object from inputs
            Birth birth = new Birth(birthPlace.getText(),
                    Short.parseShort(birthYear.getText()), Byte.parseByte(birthMonth.getText()),
                    Byte.parseByte(birthDay.getText()));
            // creating record object from input
            currentRecord = new Record(firstName.getText(), lastName.getText(), birth, gender.getSelectedItem().toString(),
                    bloodPressure.getSelectedItem().toString(), cholesterol.getSelectedItem().toString(),
                    smoker.isSelected(), Double.parseDouble(weight.getText()));
            // adding to list
            Records.getRecords().add(currentRecord);

            // then auto save if turned on
            // save that there are pending changes if auto-save is turned off
            if (Config.autoSave) {
                try {
                    Records.save();
                }
                catch (Exception ignored) { }
            }
            else {
                Main.setPendingChanges(true);
            }
            // invalidating the JTable so the content gets repainted
            Main.getEntries().repaintTable();
        }
        else { // edit already existing entries
            // copying values from fields into the object if they are different
            // true: any data has been overridden -> has to be saved
            boolean result = currentRecord.copyFrom(firstName.getText(), lastName.getText(), birthPlace.getText(),
                    Short.parseShort(birthYear.getText()), Byte.parseByte(birthMonth.getText()),
                    Byte.parseByte(birthDay.getText()), gender.getSelectedItem().toString(),
                    bloodPressure.getSelectedItem().toString(), cholesterol.getSelectedItem().toString(),
                    smoker.isSelected(), Double.parseDouble(weight.getText()));

            // if change -> save file / save that there are changes in the records
            if (result) {
                if (Config.autoSave) {
                    try {
                        Records.save();
                    }
                    catch (Exception ignored) { }
                }
                else {
                    Main.setPendingChanges(true);
                }
                Main.getEntries().repaintTable();
            }
        }
        dispose();
    }

    /**
     * No button event listener
     * @param e the event details
     */
    private void onCancel(ActionEvent e) {
        // close dialog
        dispose();
    }

    /**
     * Show the dialog
     * @param record the Record object to edit. If null -> creates a new one and adds to the list.
     */
    public void show(Record record) {
        currentRecord = record;

        if (record == null) {
            this.setTitle("Add new...");
        }
        else {
            this.setTitle("Edit existing...");
            // loading already existing data from the Record object
            firstName.setText(record.getFirstName());
            lastName.setText(record.getLastName());

            birthPlace.setText(record.getBirth().getBirthPlace());
            birthYear.setText(record.getBirth().getBirthYear().toString());
            birthMonth.setText(record.getBirth().getBirthMonth() + "");
            birthDay.setText(record.getBirth().getBirthDay() + "");

            gender.setSelectedItem(record.getGender().toString());
            weight.setText(record.getWeight() + "");
            smoker.setSelected(record.isSmoker());

            bloodPressure.setSelectedItem(record.getBloodPressureAsDisplayString());
            cholesterol.setSelectedItem(record.getCholesterol().toString());
        }

        this.pack();
        this.setVisible(true);
    }

    /**
     * Validates the field values
     * @return true if all valid, false if any invalid values are found
     */
    private boolean validateFields() {
        // checks all fields for invalidation
        return validateTextFiled(firstName)
                && validateTextFiled(lastName)
                && validateTextFiled(birthPlace)
                && validateDate()
                && validateDoubleField(weight);
    }

    /**
     * Validated the birthdate variables
     * @return true if birthdate is valid, false if it's invalid
     */
    private boolean validateDate() {
        // check if fields are valid numbers and they're supposedly in range
        if (validateIntegerField(birthYear, 0, LocalDate.now().getYear())
                && validateIntegerField(birthMonth, 1, 12)
                && validateIntegerField(birthDay, 1, 31)) {
            // parse year, month day and check if after making it into a date, values are still correct
            // if a user would define 31 April, in local date it would be May 1 as April only has 30 days
            int year = Integer.parseInt(birthYear.getText());
            int month = Integer.parseInt(birthMonth.getText());
            int day = Integer.parseInt(birthDay.getText());
            LocalDate date = LocalDate.of(year, month, day);

            return date.getYear() == year && date.getMonthValue() == month && date.getDayOfMonth() == day;
        }
        return false;
    }

    /**
     * Validates text field
     * @param field the field to be validated
     * @return true if valid, false if invalid
     */
    private boolean validateTextFiled(JTextField field) {
        return !field.getText().trim().equals("");
    }

    /**
     * Validates integer field
     * @param field the field to be validated
     * @param min the lower bounds for the data (inclusive)
     * @param max the upper bounds for the data (inclusive)
     * @return true if valid, false if invalid
     */
    private boolean validateIntegerField(JTextField field, int min, int max) {
        try {
            int value = Integer.parseInt(field.getText());
            return value >= min && value <= max;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates floating point number field
     * @param field the field to be validated
     * @return true if valid, false if invalid
     */
    private boolean validateDoubleField(JTextField field) {
        try {
            return Double.parseDouble(field.getText()) >= 0;
        }
        catch (Exception e) {
            return false;
        }
    }
}
