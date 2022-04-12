package com.exmodify.healtrecords.database;

import java.io.*;
import java.lang.reflect.Field;

public class Config {
    public static boolean showTips = true;
    public static boolean reverseNaming = true;
    public static boolean autoSave = true;

    public static void load() throws IOException, IllegalAccessException {
        if (!new File("settings.ini").exists()) {
            save();
        }
        FileReader fr = new FileReader("settings.ini");
        BufferedReader br = new BufferedReader(fr);

        Class<?> cfg = Config.class;
        Field[] cfgFields = cfg.getDeclaredFields();

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split("=", 2);
            data[0] = data[0].toLowerCase();

            for (Field field : cfgFields) {
                if (field.getName().toLowerCase().equals(data[0])) {
                    if (field.getType().getName().equalsIgnoreCase("boolean")) {
                        field.setBoolean(null, Boolean.parseBoolean(data[1]));
                    }
                }
            }
        }

        br.close();
        fr.close();
    }
    public static void save() throws IOException, IllegalAccessException {
        FileWriter fw = new FileWriter("settings.ini", false);
        fw.flush();
        BufferedWriter br = new BufferedWriter(fw);

        Class<?> cfg = Config.class;
        Field[] cfgFields = cfg.getDeclaredFields();

        for (Field field : cfgFields) {
            br.write(capitalize(field.getName()) + "=" + field.get(null));
            br.newLine();
        }
        br.close();
        fw.close();
    }

    private static String capitalize(String txt) {
        String n = txt.substring(0, 1);
        return n.toUpperCase() + txt.substring(1);
    }
}
