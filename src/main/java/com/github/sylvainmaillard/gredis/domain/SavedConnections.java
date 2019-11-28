package com.github.sylvainmaillard.gredis.domain;

import javafx.collections.ObservableList;

public interface SavedConnections {

    void add(SavedConnection savedConnection);

    void remove(SavedConnection savedConnection);

    void remove(String savedConnectionName);

    ObservableList<SavedConnection> savedConnectionsProperty();

}
