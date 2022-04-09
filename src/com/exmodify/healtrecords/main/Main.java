package com.exmodify.healtrecords.main;

import com.exmodify.healtrecords.gui.SplashScreen;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
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
}
