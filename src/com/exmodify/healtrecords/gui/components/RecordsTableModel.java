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
    String[] columns = { "Name", "Birth", "Age", "Gender", "Cholesterol", "Blood pressure", "Smoker", "Weight" };
    List<Record> records;

    public RecordsTableModel() {
        records = Records.getRecords();
    }

    @Override
    public int getRowCount() {
        return records.size();
    }
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // { "Name", "Birth", "Age", "Gender", "Cholesterol", "Blood pressure", "Smoker", "Weight" };
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

    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 2) return Integer.class;
        if (columnIndex == 7) return Double.class;

        return String.class;
    }
}
