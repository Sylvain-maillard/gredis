package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.gui.actions.NewConnectionAction;
import com.github.sylvainmaillard.gredis.gui.actions.QuitAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class MainWindow {
    private JTree tree1;
    private JPanel contentPane;
    private JFrame frame;

    public MainWindow(Settings settings) {
        frame = new JFrame("Gredis");
        frame.setJMenuBar(JMenuBuilder.MenuBarBuilder.aMenuBar(settings)
                .withMenu("main.menu.connection")
                .withItem(new NewConnectionAction(settings))
                .andSeparator()
                .andItem(new QuitAction(settings))
                .andNoMoreItems()
                .build());
    }

    public void show() {
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
