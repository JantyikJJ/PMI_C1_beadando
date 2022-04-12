package com.exmodify.healtrecords.main;

import com.exmodify.healtrecords.gui.Entries;
import com.exmodify.healtrecords.gui.SplashScreen;
import com.exmodify.healtrecords.gui.dialogs.Tips;

import java.awt.*;
import java.util.Random;

/**
 * Main class of the program
 */
public class Main {
    /**
     * Entries GUI instance
     */
    private static Entries entries;
    /**
     * Tips GUI instance
     */
    private static Tips tips;
    /**
     * Random instance
     */
    private static Random random;
    /**
     * Boolean storing if there are unsaved changes in Records List
     */
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

    /**
     * Get Entries instance
     * @return Entries instance
     */
    public static Entries getEntries() {
        return entries;
    }

    /**
     * Get Tips instance
     * @return Tips instance
     */
    public static Tips getTips() {
        return tips;
    }

    /**
     * Get Random instance
     * @return Random instance
     */
    public static Random getRandom() {
        return random;
    }

    /**
     * Get if there are pending, unsaved changes
     * @return true if there are unsaved changes, false if everything is saved
     */
    public static boolean isPendingChanges() { return pendingChanges; }

    /**
     * Set if the changes are saved or not
     * @param pendingChanges true if they are saved, false if they aren't
     */
    public static void setPendingChanges(boolean pendingChanges) { Main.pendingChanges = pendingChanges; }

    /**
     * Constructor of Main class
     * @deprecated This is the main entry of the application, only static methods are invoked.
     */
    public Main() { }
}
