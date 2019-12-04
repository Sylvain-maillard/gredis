package com.github.sylvainmaillard.gredis.domain;

import java.net.URI;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedConnection that = (SavedConnection) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(uri, that.uri) &&
                Objects.equals(auth, that.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri, auth);
    }

    public String getHost() {
        return uri.getHost();
    }

    public String getPort() {
        return String.valueOf(uri.getPort());
    }

    public String getAuth() {
        return auth;
    }

    public String getUri() {
        return this.uri.toString();
    }

    public boolean hasAuth() {
        return getAuth() != null && getAuth().length() > 0;
    }
}
