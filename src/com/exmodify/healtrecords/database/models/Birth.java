package com.exmodify.healtrecords.database.models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Birth {

    private String birthPlace;
    private Short birthYear;
    private byte birthMonth;
    private byte birthDay;

    public Birth(String birthPlace, Short birthYear, byte birthMonth, byte birthDay) {
        this.birthPlace = birthPlace;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }

    public static Birth fromNode(Element parent) {
        Element element = (Element)parent.getElementsByTagName("birth").item(0);

        String place = element.getElementsByTagName("place").item(0).getTextContent();
        Short year = Short.parseShort(element.getElementsByTagName("year").item(0)
                .getTextContent());
        byte month = Byte.parseByte(element.getElementsByTagName("month").item(0)
                .getTextContent());
        byte day = Byte.parseByte(element.getElementsByTagName("day").item(0)
                .getTextContent());
        return new Birth(place, year, month, day);
    }
    public Element toElement(Document document) {
        Element birth = document.createElement("birth");

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

    public String getBirthPlace() {
        return birthPlace;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Short getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }

    public byte getBirthMonth() {
        return birthMonth;
    }
    public void setBirthMonth(byte birthMonth) {
        this.birthMonth = birthMonth;
    }

    public byte getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(byte birthDay) {
        this.birthDay = birthDay;
    }
}
