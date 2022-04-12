package com.exmodify.healtrecords.database;

import com.exmodify.healtrecords.database.models.*;
import com.exmodify.healtrecords.database.models.Record;
import com.exmodify.healtrecords.main.Main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
    The names are from https://homepage.net/name_generator/
    The random city names are from https://www.name-generator.org.uk/?i=ovzz1ig

    The rest of code is done by János Jantyik.
 */
public class RecordsGenerator {

    /**
     * Generates n random records
     *
     * @param count the "n"; how many records should be generated
     * @param save if it should automatically save
     */
    public static void generate(int count, boolean save) {
        final String[] femaleNames = { "Abigail", "Alexandra", "Alison", "Amanda", "Amelia", "Amy", "Andrea", "Angela",
                "Anna", "Anne", "Audrey", "Ava", "Bella", "Bernadette", "Carol", "Caroline",
                "Carolyn", "Chloe", "Donna", "Dorothy" };
        final String[] maleNames = { "Adam", "Adrian", "Alan", "Alexander", "Andrew", "Anthony",
                "Austin", "Benjamin", "Blake", "Boris", "Brandon", "Brian", "Cameron", "Carl", "Charles",
                "Christian", "Christopher", "Colin", "Connor", "Dan" };

        final String[] lastNames = { "Abraham", "Allan", "Alsop", "Anderson", "Arnold", "Avery", "Bailey",
                "Baker", "Ball", "Bell", "Berry", "Black", "Blake", "Bond", "Bower", "Brown", "Buckland",
                "Burgess", "Butler", "Cameron", "Campbell", "Carr", "Chapman", "Churchill", "Clark",
                "Clarkson", "Coleman", "Cornish", "Davidson", "Davies", "Dickens", "Dowd", "Duncan", "Dyer",
                "Edmunds", "Ellison", "Ferguson", "Fisher", "Forsyth", "Fraser" };

        final String[] cities = { "Falls Church (US)", "Coventry (UK)", "Saint Anthony (CA)", "Kettering (UK)",
                "Parma (US)", "Orford (UK)", "Bideford (UK)", "Brooks (CA)", "Oklahoma City (US)",
                "Santa Rosa (US)", "Neosho (US)", "Harwich (US)", "Gosport (UK)",
                "Hornsey (UK)", "Opelika (US)", "Lutterworth (UK)", "Godalming (UK)",
                "Saint-Eustache (CA)", "Denison (US)", "Emmitsburg (US)", "Loxton (AU)", "French Lick (US)",
                "Livingston (US)", "Ilfracombe (UK)", "Rantoul (US)", "Wolverhampton (UK)", "Gallup (US)",
                "Uckfield (UK)", "Wolverton and Greenleys (UK)", "Hinton (US)", "Peru (US)", "Hessle (UK)",
                "Hollywood (US)", "Scottsdale (US)", "Willenhall (UK)", "Lompoc (US)", "Ampthill (UK)", "Val-d (CA)",
                "Cooma (AU)", "Epworth (UK)", "Sterling (US)", "Townsville (AU)", "Stony Point (US)",
                "West Memphis (US)", "Taree (AU)", "Woodstock (CA)", "Oak Ridge (US)",
                "Armidale (AU)", "Helmsley (UK)", "Channel-Port aux Basques (CA)" };

        Random rng = Main.getRandom();
        List<Record> records = Records.getRecords();

        LocalDate birthdayStart = LocalDate.of(1960, 1, 1);
        LocalDate birthdayEnd = LocalDate.now();

        for (int i = 0; i < count; i++) {
            String firstName;
            Gender gender = Gender.Female;
            if (rng.nextInt(10) > 4) {
                firstName = maleNames[rng.nextInt(maleNames.length)];
                gender = Gender.Male;
            }
            else {
                firstName = femaleNames[rng.nextInt(femaleNames.length)];
            }
            Birth birth = new Birth(cities[rng.nextInt(cities.length)],
                    LocalDate.ofEpochDay(rng.nextLong(birthdayStart.toEpochDay(), birthdayEnd.toEpochDay())));

            List<BloodPressure> bloodPressureList = new ArrayList<>();
            int bpRandom = rng.nextInt(90);
            if (bpRandom > 80) {
                bloodPressureList.add(BloodPressure.LowDiastolic);
                bloodPressureList.add(BloodPressure.HighSystolic);
            }
            else if (bpRandom > 70) {
                bloodPressureList.add(BloodPressure.HighDiastolic);
                bloodPressureList.add(BloodPressure.LowSystolic);

            }
            else if (bpRandom > 60) {
                bloodPressureList.add(BloodPressure.LowDiastolic);
                bloodPressureList.add(BloodPressure.LowSystolic);

            }
            else if (bpRandom > 50) {
                bloodPressureList.add(BloodPressure.LowSystolic);

            }
            else if (bpRandom > 40) {
                bloodPressureList.add(BloodPressure.LowDiastolic);

            }
            else if (bpRandom > 30) {
                bloodPressureList.add(BloodPressure.HighDiastolic);
                bloodPressureList.add(BloodPressure.HighSystolic);
            }
            else if (bpRandom > 20) {
                bloodPressureList.add(BloodPressure.HighSystolic);
            }
            else if (bpRandom > 10) {
                bloodPressureList.add(BloodPressure.HighDiastolic);
            }
            else {
                bloodPressureList.add(BloodPressure.Normal);
            }
            EnumSet<BloodPressure> bloodPressures = EnumSet.copyOf(bloodPressureList);

            Cholesterol cholesterol = Cholesterol.Normal;
            int cRandom = rng.nextInt(30);
            if (cRandom > 20) {
                cholesterol = Cholesterol.High;
            }
            else if (cRandom > 10) {
                cholesterol = Cholesterol.Low;
            }

            records.add(new Record(firstName, lastNames[rng.nextInt(lastNames.length)], birth, gender, bloodPressures,
                    cholesterol, rng.nextBoolean(), rng.nextDouble(45, 120)));
        }

        if (save) {
            try {
                Records.save();
            }
            catch (Exception ignored) { }
        }
    }
}
