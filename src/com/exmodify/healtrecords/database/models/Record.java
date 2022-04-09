package com.exmodify.healtrecords.database.models;

import com.exmodify.healtrecords.database.Records;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Record {
    private String firstName;
    private String lastName;

    private Birth birth;
    private Gender gender;
    private BloodPressure bloodPressure;
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

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }
    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = BloodPressure.valueOf(bloodPressure);
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
        Records.appendElement("bloodPressure", this.bloodPressure, document, record);
        Records.appendElement("cholesterol", this.cholesterol, document, record);
        Records.appendElement("smoker", this.smoker, document, record);
        Records.appendElement("weight", this.weight, document, record);


        return record;
    }
}
