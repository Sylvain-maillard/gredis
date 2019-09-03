package com.github.sylvainmaillard.gredis;

import com.github.sylvainmaillard.gredis.domain.Logging;
import com.github.sylvainmaillard.gredis.gui.Settings;
import com.github.sylvainmaillard.gredis.gui.MainWindow;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Gredis {

    public static void main(String[] args) {

        // load settings
        Settings settings = new Settings();

        // setup l&f before creating mainwindow
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticLookAndFeel");
//                    UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
//                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
                new MainWindow(settings).show();
            } catch (Exception e) {
                Logging.log(e);
                showMessageDialog(null, "Oups... something went wrong:\n " + e.getMessage(), "Gredis", ERROR_MESSAGE);
            }
        });

    }
}
