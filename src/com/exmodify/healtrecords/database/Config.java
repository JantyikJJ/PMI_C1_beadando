package com.exmodify.healtrecords.database;

import java.io.*;
import java.lang.reflect.Field;

/**
 * The static configuration class
 */
public class Config {
    /**
     * Whether the Tips dialog should automatically open on application load
     */
    public static boolean showTips = true;
    /**
     * Whether the first and last naming should be reversed (like in Hungary)
     */
    public static boolean reverseNaming = true;
    /**
     * Whether the Records should be saved automatically after data edit
     */
    public static boolean autoSave = true;

    /**
     * Load configuration
     *
     * @throws IOException if the file was failed to be opened or read
     * @throws IllegalAccessException if property specified in the configuration file was inaccessible / unmodifiable
     */
    public static void load() throws IOException, IllegalAccessException {
        // if the configuration file doesn't exist, create a skeleton
        if (!new File("settings.ini").exists()) {
            save();
        }
        // initialize file reader
        FileReader fr = new FileReader("settings.ini");
        BufferedReader br = new BufferedReader(fr);

        // get class fields using reflect
        Class<?> cfg = Config.class;
        Field[] cfgFields = cfg.getDeclaredFields();

        // read configuration file line by line
        String line;
        while ((line = br.readLine()) != null) {
            // parse property (data[0]) and value (data[1])
            String[] data = line.split("=", 2);
            data[0] = data[0].toLowerCase();

            // go through all the queryable fields
            for (Field field : cfgFields) {
                // check if name matches
                if (field.getName().toLowerCase().equals(data[0])) {
                    // check if the type is boolean of the property
                    // if other savable properties were in the Config class
                    // they would have to be defined like this
                    // for example if there was an integer value
                    // these lines would need to be duplicated and adjusted accordingly (properly parsing)
                    if (field.getType().getName().equalsIgnoreCase("boolean")) {
                        field.setBoolean(null, Boolean.parseBoolean(data[1]));
                    }
                }
            }
        }

        // close readers
        br.close();
        fr.close();
    }

    /**
     * Saves configuration
     *
     * @throws IOException if the file couldn't be opened / flushed / written
     * @throws IllegalAccessException if the queried field values couldn't be accessed
     */
    public static void save() throws IOException, IllegalAccessException {
        // open file writer and flush the contents
        FileWriter fw = new FileWriter("settings.ini", false);
        fw.flush();
        BufferedWriter br = new BufferedWriter(fw);

        // get fields with dynamically with reflection
        Class<?> cfg = Config.class;
        Field[] cfgFields = cfg.getDeclaredFields();

        // iterate through fields and write them into the configuration file line-by-line
        for (Field field : cfgFields) {
            br.write(capitalize(field.getName()) + "=" + field.get(null));
            br.newLine();
        }
        // close file writers
        br.close();
        fw.close();
    }

    /**
     * Capitalize the given String (only the very first letter)
     *
     * @param txt the String to be capitalized
     * @return the capitalized String
     */
    private static String capitalize(String txt) {
        // get first character as String
        String n = txt.substring(0, 1);
        // make first character uppercase and append the rest of the String to it
        return n.toUpperCase() + txt.substring(1);
    }

    /**
     * Config constructor.
     *
     * @deprecated This class has only static methods thus instancing is unnecessary.
     */
    public Config() { }
}
