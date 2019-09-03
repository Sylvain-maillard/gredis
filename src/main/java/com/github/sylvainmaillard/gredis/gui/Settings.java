package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.Logging;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class Settings {

    private final Locale locale;
    private final ResourceBundle labels;

    public Settings(Locale locale) {
        this.locale = locale;
        labels = ResourceBundle.getBundle("gui.labels", locale);
    }

    public Settings() {
        this(Locale.getDefault());
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLabel(String object) {
        return labels.getString(object + ".label");
    }

    public String getDesc(String object) {
        return labels.getString(object + ".desc");
    }

    public KeyStroke getMnemonic(String object) {
        if (labels.containsKey(object + ".mnemonic")) {
            return KeyStroke.getKeyStroke(labels.getString(object + ".mnemonic"));
        } else {
            return null;
        }
    }
}
