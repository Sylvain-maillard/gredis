package com.github.sylvainmaillard.gredis.domain;

import java.net.URI;

public class SavedConnection {
    private final String name;
    private final URI uri;
    private final String auth;

    public SavedConnection(String name, URI uri, String auth) {
        this.name = name;
        this.uri = uri;
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
