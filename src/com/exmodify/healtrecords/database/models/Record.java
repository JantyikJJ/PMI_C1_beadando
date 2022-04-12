package com.exmodify.healtrecords.database.models;

import com.exmodify.healtrecords.database.Records;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Record class holding every piece of information altogether
 */
public class Record {
    /**
     * First name of the person
     */
    private String firstName;
    /**
     * Last name of the person
     */
    private String lastName;

    /**
     * Birth information of the person
     */
    private final Birth birth;
    /**
     * Gender of the person
     */
    private Gender gender;
    /**
     * Blood pressure information about the person
     */
    private EnumSet<BloodPressure> bloodPressure;
    /**
     * Cholesterol level of the person
     */
    private Cholesterol cholesterol;

    /**
     * Stores whether the person is smoking. True if they are, false if they aren't.
     */
    private boolean smoker;
    /**
     * Weight of the person
     */
    private double weight;

    /**
     * Initialize a new Record object
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param birth The object that contains the birth data of the person
     * @param gender The gender of the person as String (Male, Female)
     * @param bloodPressure The blood pressure of the person as String (enum values concatenated by a comma)
     * see for values: {@link com.exmodify.healtrecords.database.models.BloodPressure}
     * @param cholesterol The cholesterol level of the person as String (Normal, High, Low)
     * @param smoker True if the person is smoking, false if they aren't
     * @param weight The weight of the person
     *
     */
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

    /**
     * Initialize Record object
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param birth The object that contains the birth data of the person
     * @param gender The gender of the person
     * @param bloodPressure The blood pressure of the person
     * @param cholesterol The cholesterol level of the person
     * @param smoker True if the person is smoking, false if they aren't
     * @param weight The weight of the person
     */
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

    /**
     * Get first name from the Record
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set first name for the Record
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get last name from the Record
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set last name for the Record
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the Birth object of this Record
     *
     * @return the Birth object
     */
    public Birth getBirth() {
        return birth;
    }

    /**
     * Get the gender of this Record
     * @return the Gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set the gender for this Record from String
     * @param gender the gender String (Male, Female)
     */
    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender);
    }

    /**
     * Set the blood pressure enums for this Record
     * @param bloodPressure the EnumSet containing the blood pressure data
     */
    public void setBloodPressure(EnumSet<BloodPressure> bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    /**
     * Set the blood pressure enums for this Record from a display String (concatenated by comma and a space; if both
     * systolic and diastolic are either low or high, then it's abbreviated as Low or High respectively)
     * @param bloodPressure the String containing the blood pressure data
     */
    public void setBloodPressureFromDisplayString(String bloodPressure) {
        // remove spaces
        String prepared = bloodPressure.replace(" ", "");
        // if High -> both diastolic and systolic are high  (nothing else can be next to High)
        if (prepared.equals("High")) {
            prepared = "HighDiastolic,HighSystolic";
        }
        // if Low -> both diastolic and systolic are low (nothing else can be next to Low)
        else if (prepared.equals("Low")) {
            prepared = "LowDiastolic,LowSystolic";
        }
        // now the prepared String contains the proper naming scheme where all the values are 1:1 match to the original
        setBloodPressure(prepared);
    }

    /**
     * Set blood pressure enums for this record from a String (concatenated by single comma; the values match the
     * original enum namings)
     * @param bloodPressure the String containing the blood pressure data
     */
    public void setBloodPressure(String bloodPressure) {
        // split parts by comma
        String[] flagValues = bloodPressure.split(",");
        List<BloodPressure> flags = new ArrayList<>();

        // go through parts and parse Enum objects out of them, then add them to a List
        for (String value : flagValues) {
            flags.add(BloodPressure.valueOf(value));
        }
        // convert List to an EnumSet and set the value of the Record
        EnumSet<BloodPressure> set = EnumSet.copyOf(flags);
        setBloodPressure(set);
    }

    /**
     * Stringify the Blood pressure data as a display String
     * - Shorts values if both diastolic and systolic values are high or low to High or Low respectively
     * - Concatenates values by comma and a space for visuals
     *
     * @return the blood pressure data as display String
     */
    public String getBloodPressureAsDisplayString() {
        List<String> parts = new ArrayList<>();

        // if there's normal present -> it only can be Normal
        if (bloodPressure.contains(BloodPressure.Normal)) {
            return "Normal";
        }
        // if there's both high diastolic and high systolic is present -> it only can be High
        if (bloodPressure.contains(BloodPressure.HighDiastolic) && bloodPressure.contains(BloodPressure.HighSystolic)) {
            return "High";
        }
        // if there's both low diastolic and high low is present -> it only can be Low
        if (bloodPressure.contains(BloodPressure.LowDiastolic) && bloodPressure.contains(BloodPressure.LowSystolic)) {
            return "Low";
        }

        // build string based on EnumSet's values
        // since it is possible that a person has high diastolic and low systolic, or vice versa
        // or maybe that only one of the values are high or low -> these build-ups are necessary
        // this part checks if specific enums are present in the EnumSet
        // and if they are, it puts the associated naming into a List of Strings
        if (bloodPressure.contains(BloodPressure.HighDiastolic)) {
            parts.add("High Diastolic");
        }
        if (bloodPressure.contains(BloodPressure.HighSystolic)) {
            parts.add("High Systolic");
        }

        if (bloodPressure.contains(BloodPressure.LowDiastolic)) {
            parts.add("Low Diastolic");
        }
        if (bloodPressure.contains(BloodPressure.LowSystolic)) {
            parts.add("Low Systolic");
        }

        // joins the generated list of string by a comma and a space
        return String.join(", ", parts);
    }

    /**
     * Gets the Cholesterol of the current Record
     * @return the Cholesterol enum
     */
    public Cholesterol getCholesterol() {
        return cholesterol;
    }

    /**
     * Sets the Cholesterol for this Record from a String
     * @param cholesterol the cholesterol as String (can be Low, Normal or High)
     */
    public void setCholesterol(String cholesterol) {
        this.cholesterol = Cholesterol.valueOf(cholesterol);
    }

    /**
     * Gets if the person associated to this Record is a smoker
     * @return true if the person is smoker, false if they aren't
     */
    public boolean isSmoker() {
        return smoker;
    }

    /**
     * Sets whether the person is a smoker
     * @param smoker true if the person is a smoker, false if they aren't
     */
    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    /**
     * Get the weight of the person associated to this Record
     * @return the weight of the person
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set the weight of the person associated to this Record
     * @param weight the weight of the person
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Generate XML element out of this Record
     * @param document the document what will contain the XML Element
     * @return the generated Element
     * 
     * @see com.exmodify.healtrecords.database.models.Birth#toElement(Document) 
     * @see com.exmodify.healtrecords.database.Records#appendElement(String, Object, Document, Element)
     */
    public Element toElement(Document document) {
        // create container element for the record
        Element record = document.createElement("record");

        // create element for the first name containing the first name of the record
        Records.appendElement("firstName", this.firstName, document, record);
        // create element for the last name containing the last name of the record
        Records.appendElement("lastName", this.lastName, document, record);
        // create element of the Birth object and add it to the record element
        record.appendChild(birth.toElement(document));
        // create element for the gender containing the gender of the record
        Records.appendElement("gender", this.gender, document, record);
        // build a blood pressure string concatenated by commas and create an element out of it
        List<String> bp = new ArrayList<>();
        for (BloodPressure pressure : this.bloodPressure) {
            bp.add(pressure.toString());
        }
        Records.appendElement("bloodPressure", String.join(",", bp), document, record);
        // create element of the cholesterol enum and then it to the record element
        Records.appendElement("cholesterol", this.cholesterol, document, record);
        // create element for smoker value and store if the person is smoker or not (true / false)
        // then add it to the record element
        Records.appendElement("smoker", this.smoker, document, record);
        // create element for weight and sture weight data, then add it to the record element
        Records.appendElement("weight", this.weight, document, record);


        return record;
    }

    /**
     * Copy data into the Record object, if there are any changes
     *
     * @param firstName new first name
     * @param lastName new last name
     * @param birthPlace new birthplace
     * @param birthYear new birth year
     * @param birthMonth new birth month
     * @param birthDay new birthday
     * @param gender new gender (Male or Female)
     * @param bloodPressure new blood pressure from display string, see
     * {@link #setBloodPressureFromDisplayString(String)},
     * {@link #getBloodPressureAsDisplayString()}
     * @param cholesterol new cholesterol as String (Low, Normal or High)
     * @param smoker new smoker
     * @param weight new weight
     * @return true if there are any data changes, false if all the data are identical
     */
    public boolean copyFrom(String firstName, String lastName, String birthPlace, Short birthYear,byte birthMonth,
                            byte birthDay, String gender, String bloodPressure,
                            String cholesterol, boolean smoker, double weight) {
        boolean modified = false;
        // checks if the values from parameter are the same as the one the Record contains
        // if they aren't, they get overridden and 'modified' boolean is set true
        // then it's returned

        if (!firstName.equals(getFirstName())) {
            modified = true;
            setFirstName(firstName);
        }
        if (!lastName.equals(getLastName())) {
            modified = true;
            setLastName(lastName);
        }
        if (!birthPlace.equals(birth.getBirthPlace())) {
            modified = true;
            birth.setBirthPlace(birthPlace);
        }
        if (!birthYear.equals(birth.getBirthYear())) {
            modified = true;
            birth.setBirthYear(birthYear);
        }
        if (birthMonth != birth.getBirthMonth()) {
            modified = true;
            birth.setBirthMonth(birthMonth);
        }
        if (birthDay != birth.getBirthDay()) {
            modified = true;
            birth.setBirthDay(birthDay);
        }
        if (!this.gender.toString().equalsIgnoreCase(gender)) {
            modified = true;
            setGender(gender);
        }
        if (!getBloodPressureAsDisplayString().equalsIgnoreCase(bloodPressure)) {
            modified = true;
            setBloodPressureFromDisplayString(bloodPressure);
        }
        if (!getCholesterol().toString().equalsIgnoreCase(cholesterol)) {
            modified = true;
            setCholesterol(cholesterol);
        }
        if (smoker != isSmoker()) {
            modified = true;
            setSmoker(smoker);
        }
        if (weight != getWeight()) {
            modified = true;
            setWeight(weight);
        }

        return modified;
    }
}
