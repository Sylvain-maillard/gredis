package com.github.sylvainmaillard.gredis.domain;

public class SavedConnection {
    private final String name;

    public SavedConnection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
