package com.exmodify.healtrecords.database.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Birth object containing all the birth-related data for a Record
 */
public class Birth {

    /**
     * Place of birth
     */
    private String birthPlace;
    /**
     * Year of birth
     */
    private Short birthYear;
    /**
     * Month of birth
     */
    private byte birthMonth;
    /**
     * Day of birth
     */
    private byte birthDay;

    /**
     * Create new Birth object for a Record
     * @param birthPlace the birthplace
     * @param birthYear the birth year
     * @param birthMonth the birth month
     * @param birthDay the day of birth
     */
    public Birth(String birthPlace, Short birthYear, byte birthMonth, byte birthDay) {
        this.birthPlace = birthPlace;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    /**
     * Create new Birth object for a Record
     * @param birthPlace the birthplace
     * @param date the LocalDate containing the birthday data (day, month, year)
     */
    public Birth(String birthPlace, LocalDate date) {
        this.birthPlace = birthPlace;
        this.birthYear = (short)date.getYear();
        this.birthMonth = (byte)date.getMonthValue();
        this.birthDay = (byte)date.getDayOfMonth();
    }

    /**
     * Parses an XML Node and created a Birth object
     * @param parent the Record element
     * @return the parsed Birth object
     */
    public static Birth fromNode(Element parent) {
        // get Birth element from Record element
        Element element = (Element)parent.getElementsByTagName("birth").item(0);

        // get place, year, month and day values from the Birth Element and return it as a new Birth object
        String place = element.getElementsByTagName("place").item(0).getTextContent();
        Short year = Short.parseShort(element.getElementsByTagName("year").item(0)
                .getTextContent());
        byte month = Byte.parseByte(element.getElementsByTagName("month").item(0)
                .getTextContent());
        byte day = Byte.parseByte(element.getElementsByTagName("day").item(0)
                .getTextContent());
        return new Birth(place, year, month, day);
    }

    /**
     * Creates and XML Element out of the Birth object
     * @param document the parent Document
     * @return the Birth Element that can be added to a Record Element
     */
    public Element toElement(Document document) {
        // Create main Birth element
        Element birth = document.createElement("birth");

        // Below creates 4 different Elements inside the Birth element
        // And puts the appropriate values into them (place, year, month, day)
        // then returns the Birth Element
        Element place = document.createElement("place");
        place.appendChild(document.createTextNode(this.birthPlace));
        birth.appendChild(place);

        Element year = document.createElement("year");
        year.appendChild(document.createTextNode(this.birthYear + ""));
        birth.appendChild(year);

        Element month = document.createElement("month");
        month.appendChild(document.createTextNode(this.birthMonth + ""));
        birth.appendChild(month);

        Element day = document.createElement("day");
        day.appendChild(document.createTextNode(this.birthDay + ""));
        birth.appendChild(day);

        return birth;
    }

    /**
     * Get birthplace
     * @return the birthplace
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * Set birthplace
     * @param birthPlace the new birthplace
     */
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    /**
     * Get year of birth
     * @return year of birth
     */
    public Short getBirthYear() {
        return birthYear;
    }

    /**
     * Set new year of birth
     * @param birthYear new year of birth
     */
    public void setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }

    /**
     * Get month of birth
     * @return month of birth
     */
    public byte getBirthMonth() {
        return birthMonth;
    }

    /**
     * Set new month of birth
     * @param birthMonth new month of birth
     */
    public void setBirthMonth(byte birthMonth) {
        this.birthMonth = birthMonth;
    }

    /**
     * Get day of birth
     * @return the day of birth
     */
    public byte getBirthDay() {
        return birthDay;
    }

    /**
     * Set new day of birth
     * @param birthDay new day of birth
     */
    public void setBirthDay(byte birthDay) {
        this.birthDay = birthDay;
    }

    /**
     * Formats Birth as Place, Year/Month/Day
     * @return formatted String
     */
    @Override
    public String toString() {
        return birthPlace + ", " + birthYear + "/" + birthMonth + "/" + birthDay;
    }

    /**
     * Check if two Birth objects are equal by properties
     * @param o the other Birth object
     * @return true if values are equal, false if there's any difference
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Birth birth = (Birth) o;
        return birthMonth == birth.birthMonth && birthDay == birth.birthDay && birthPlace.equals(birth.birthPlace)
                && birthYear.equals(birth.birthYear);
    }

    /**
     * AUTO-GENERATED: generates unique hash code
     * @return the unique hash code generated from birthplace, year, month and day
     */
    @Override
    public int hashCode() {
        return Objects.hash(birthPlace, birthYear, birthMonth, birthDay);
    }
}
