package com.exmodify.healtrecords.main;

import com.exmodify.healtrecords.gui.Entries;
import com.exmodify.healtrecords.gui.SplashScreen;
import com.exmodify.healtrecords.gui.dialogs.Tips;

import java.awt.*;
import java.util.Random;

public class Main {
    private static Entries entries;
    private static Tips tips;
    private static Random random;
    private static boolean pendingChanges;

    /**
     * Main entry of the program
     * @param args the console arguments which aren't used in this case
     */
    public static void main(String[] args) {
        pendingChanges = false;
        random = new Random(new Random().nextInt());

        entries = new Entries();
        tips = new Tips();

        // Invoking splash screen on dispatch thread asynchronously
        EventQueue.invokeLater(() -> {
            try {
                SplashScreen splash = new SplashScreen();
                splash.show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static Entries getEntries() {
        return entries;
    }
    public static Tips getTips() {
        return tips;
    }
    public static Random getRandom() {
        return random;
    }
    public static boolean isPendingChanges() { return pendingChanges; }
    public static void setPendingChanges(boolean pendingChanges) { Main.pendingChanges = pendingChanges; }
}
