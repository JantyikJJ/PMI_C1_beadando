package com.exmodify.healtrecords.database.models;

import com.exmodify.healtrecords.database.Records;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Record {
    private String firstName;
    private String lastName;

    private Birth birth;
    private Gender gender;
    private EnumSet<BloodPressure> bloodPressure;
    private Cholesterol cholesterol;

    private boolean smoker;
    private double weight;

    public Record(String firstName, String lastName, Birth birth, String gender, String bloodPressure,
                  String cholesterol, boolean smoker, double weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birth = birth;
        setGender(gender);
        setBloodPressure(bloodPressure);
        setCholesterol(cholesterol);
        this.smoker = smoker;
        this.weight = weight;
    }
    public Record(String firstName, String lastName, Birth birth, Gender gender, EnumSet<BloodPressure> bloodPressure,
                  Cholesterol cholesterol, boolean smoker, double weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birth = birth;
        this.gender = gender;
        this.bloodPressure = bloodPressure;
        this.cholesterol = cholesterol;
        this.smoker = smoker;
        this.weight = weight;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Birth getBirth() {
        return birth;
    }
    public void setBirth(String place, Short year, byte month, byte day) {
        this.birth = new Birth(place, year, month, day);
    }

    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender);
    }

    public EnumSet<BloodPressure> getBloodPressure() {
        return bloodPressure;
    }
    public void setBloodPressure(EnumSet<BloodPressure> bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    public void setBloodPressure(String bloodPressure) {
        String[] flagValues = bloodPressure.split(",");
        List<BloodPressure> flags = new ArrayList<>();
        for (String value : flagValues) {
            flags.add(BloodPressure.valueOf(value));
        }
        EnumSet<BloodPressure> set = EnumSet.copyOf(flags);
        setBloodPressure(set);
    }
    public String getBloodPressureAsDisplayString() {
        List<String> parts = new ArrayList<>();

        if (bloodPressure.contains(BloodPressure.Normal)) {
            return "Normal";
        }
        if (bloodPressure.contains(BloodPressure.HighDiastolic) && bloodPressure.contains(BloodPressure.HighSystolic)) {
            return "High";
        }
        if (bloodPressure.contains(BloodPressure.LowDiastolic) && bloodPressure.contains(BloodPressure.LowSystolic)) {
            return "Low";
        }

        if (bloodPressure.contains(BloodPressure.HighDiastolic)) {
            parts.add("High diastolic");
        }
        if (bloodPressure.contains(BloodPressure.HighSystolic)) {
            parts.add("High systolic");
        }

        if (bloodPressure.contains(BloodPressure.LowDiastolic)) {
            parts.add("Low diastolic");
        }
        if (bloodPressure.contains(BloodPressure.LowSystolic)) {
            parts.add("Low systolic");
        }


        return String.join(", ", parts);
    }

    public Cholesterol getCholesterol() {
        return cholesterol;
    }
    public void setCholesterol(Cholesterol cholesterol) {
        this.cholesterol = cholesterol;
    }
    public void setCholesterol(String cholesterol) {
        this.cholesterol = Cholesterol.valueOf(cholesterol);
    }

    public boolean isSmoker() {
        return smoker;
    }
    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Element toElement(Document document) {
        Element record = document.createElement("record");


        Records.appendElement("firstName", this.firstName, document, record);
        Records.appendElement("lastName", this.lastName, document, record);
        record.appendChild(birth.toElement(document));
        Records.appendElement("gender", this.gender, document, record);
        List<String> bp = new ArrayList<>();
        for (BloodPressure pressure : this.bloodPressure) {
            bp.add(pressure.toString());
        }

        Records.appendElement("bloodPressure", String.join(",", bp), document, record);
        Records.appendElement("cholesterol", this.cholesterol, document, record);
        Records.appendElement("smoker", this.smoker, document, record);
        Records.appendElement("weight", this.weight, document, record);


        return record;
    }
}
