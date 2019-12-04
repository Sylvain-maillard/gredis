package com.github.sylvainmaillard.gredis.domain;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public interface SavedConnections {

    void add(SavedConnection savedConnection);

    void remove(SavedConnection savedConnection);

    void remove(String savedConnectionName);

    ObjectProperty<ObservableList<SavedConnection>> savedConnectionsProperty();

    void replace(SavedConnection oldConnection, SavedConnection newConnection);

    boolean doesNotContains(SavedConnection savedConnection);
}
