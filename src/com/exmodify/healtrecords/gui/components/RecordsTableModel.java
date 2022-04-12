package com.exmodify.healtrecords.gui.components;

import com.exmodify.healtrecords.database.Config;
import com.exmodify.healtrecords.database.Records;
import com.exmodify.healtrecords.database.models.Cholesterol;
import com.exmodify.healtrecords.database.models.Gender;
import com.exmodify.healtrecords.database.models.Record;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class RecordsTableModel extends AbstractTableModel {
    // define column values
    final String[] columns = { "Name", "Birth", "Age", "Gender", "Cholesterol", "Blood pressure", "Smoker", "Weight" };
    // save records for ease of access (it's only reference anyway)
    List<Record> records;

    /**
     * The JTable Data model for Records
     */
    public RecordsTableModel() {
        records = Records.getRecords();
    }

    /**
     * Data length
     * @return how many data are present in the Records list
     */
    @Override
    public int getRowCount() {
        return records.size();
    }

    /**
     * Column length
     * @return REturn how many columns are defined (String[] columns constant)
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Gets value for a specific row and column
     * @param rowIndex the index of the row (Record itself)
     * @param columnIndex the index of the column (which property { "Name", "Birth", "Age",
     *                    "Gender", "Cholesterol", "Blood pressure", "Smoker", "Weight" })
     * @return the value of columnIndex-th property of the rowIndex-th Record
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // get record at row
        Record record = records.get(rowIndex);
        switch (columnIndex) {
            case 0: // Name
                if (Config.reverseNaming) return record.getLastName() + " " + record.getFirstName();
                return record.getFirstName() + " " + record.getLastName();
            case 1: // Birth
                return record.getBirth().toString();
            case 2: // Age
                LocalDate d = LocalDate.now()
                        .minusYears(record.getBirth().getBirthYear())
                        .minusMonths(record.getBirth().getBirthMonth())
                        .minusDays(record.getBirth().getBirthDay());

                return d.getYear();
            case 3: // Gender
                return record.getGender().toString();
            case 4: // Cholesterol
                return record.getCholesterol().toString();
            case 5: // Blood pressure
                return record.getBloodPressureAsDisplayString();
            case 6: // Smoker
                return record.isSmoker() ? "Yes" : "No";
            case 7: // Weight
                return record.getWeight();
        }
        return null;
    }

    /**
     * Get name of the column
     * @param columnIndex the index of the column
     * @return the column name under columnIndex
     */
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    /**
     * Return the class type of the property under the specific column
     * @param columnIndex the column index
     * @return the Class of the type of object
     */
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 2) return Integer.class;
        if (columnIndex == 7) return Double.class;

        return String.class;
    }
}
