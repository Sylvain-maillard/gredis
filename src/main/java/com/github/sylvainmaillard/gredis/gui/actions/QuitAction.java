package com.github.sylvainmaillard.gredis.gui.actions;

import com.github.sylvainmaillard.gredis.gui.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class QuitAction extends Action {

    public QuitAction(Settings settings) {
        super(settings);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
