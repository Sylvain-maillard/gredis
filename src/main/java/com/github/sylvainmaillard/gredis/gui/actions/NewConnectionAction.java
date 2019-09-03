package com.github.sylvainmaillard.gredis.gui.actions;

import com.github.sylvainmaillard.gredis.gui.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewConnectionAction extends Action {

    public NewConnectionAction(Settings settings) {
        super(settings);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "WIP");
    }
}
