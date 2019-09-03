package com.github.sylvainmaillard.gredis.gui.actions;

import com.github.sylvainmaillard.gredis.gui.Settings;

import javax.swing.*;

public abstract class Action extends AbstractAction {

    public Action(Settings settings) {
        putValue(SHORT_DESCRIPTION, settings.getDesc(this.getClass().getName()));
        putValue(NAME, settings.getLabel(this.getClass().getName()));
        putValue(ACCELERATOR_KEY, settings.getMnemonic(this.getClass().getName()));
    }
}
