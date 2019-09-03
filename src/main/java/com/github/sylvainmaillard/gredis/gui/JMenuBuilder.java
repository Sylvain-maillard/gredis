package com.github.sylvainmaillard.gredis.gui;

import javax.swing.*;

public class JMenuBuilder  {

    static class Impl implements JMenuBuilder.MenuBarBuilder, JMenuBuilder.MenuBuilder, JMenuBuilder.MenuItemBuilder {

        private final Settings settings;
        JMenuBar menuBar = new JMenuBar();
        JMenu currentMenu;
        JMenuItem currentMenuItem;

        public Impl(Settings settings) {
            this.settings = settings;
        }

        @Override
        public MenuBuilder withMenu(String labelCode) {
            currentMenu = new JMenu();
            menuBar.add(currentMenu);
            currentMenu.setText(settings.getLabel(labelCode));
            currentMenu.setMnemonic(settings.getMnemonic(labelCode).getKeyCode());
            return this;
        }

        @Override
        public JMenuBar build() {
            return menuBar;
        }

        @Override
        public MenuItemBuilder withItem(Action action) {
            currentMenuItem = new JMenuItem();
            currentMenu.add(currentMenuItem);
            currentMenuItem.setAction(action);
            return this;
        }

        @Override
        public MenuItemBuilder andItem(Action action) {
            this.withItem(action);
            return this;
        }

        @Override
        public MenuBarBuilder andNoMoreItems() {
            return this;
        }

        @Override
        public MenuBuilder andSeparator() {
            currentMenu.add(new JSeparator());
            return this;
        }
    }

    interface MenuBarBuilder {
        static MenuBarBuilder aMenuBar(Settings settings) {
            return new Impl(settings);
        }
        MenuBuilder withMenu(String labelCode);
        JMenuBar build();
    }
    interface MenuBuilder {
        MenuItemBuilder withItem(Action action);
        MenuItemBuilder andItem(Action action);
    }
    interface MenuItemBuilder {
        MenuBarBuilder andNoMoreItems();
        MenuBuilder andSeparator();
    }

}
