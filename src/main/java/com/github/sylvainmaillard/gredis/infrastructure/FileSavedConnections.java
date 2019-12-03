package com.github.sylvainmaillard.gredis.infrastructure;

import com.github.sylvainmaillard.gredis.domain.SavedConnection;
import com.github.sylvainmaillard.gredis.domain.SavedConnections;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FileSavedConnections implements SavedConnections {

    private static final String GREDIS_CONNECTIONS_FILE = ".gredis-connections.json";

    private final ObservableList<SavedConnection> connections = FXCollections.observableArrayList();
    private final BooleanProperty booleanProperty = new SimpleBooleanProperty();

    public FileSavedConnections() {
        if (configFilePath().toFile().exists()) {
            readFromFile();
        }
        booleanProperty.setValue(connections.isEmpty());
        connections.addListener((ListChangeListener<SavedConnection>) c -> booleanProperty.setValue(connections.isEmpty()));
    }

    @Override
    public void add(SavedConnection savedConnection) {
        this.connections.add(savedConnection);
        saveToFile();
    }

    @Override
    public void remove(SavedConnection savedConnection) {
        this.connections.remove(savedConnection);
        saveToFile();
    }

    @Override
    public void remove(String savedConnectionName) {
        this.connections.removeIf(savedConnection -> savedConnection.getName().equals(savedConnectionName));
        saveToFile();
    }

    @Override
    public ObjectProperty<ObservableList<SavedConnection>> savedConnectionsProperty() {
        return new SimpleObjectProperty<>(new SimpleListProperty<>(this.connections));
    }

    @Override
    public BooleanProperty emptynessProperty() {
        return booleanProperty;
    }

    @Override
    public void replace(SavedConnection oldConnection, SavedConnection newConnection) {
        this.connections.replaceAll(savedConnection -> {
            if(savedConnection.equals(oldConnection)) {
                return newConnection;
            }
            return savedConnection;
        });
    }

    private Path configFilePath() {
        return Path.of(System.getProperty("user.home")).resolve(Path.of(GREDIS_CONNECTIONS_FILE));
    }

    private void readFromFile() {
        try {
            String json = Files.readString(configFilePath());
            Type listType = new TypeToken<List<SavedConnection>>() {
            }.getType();
            this.connections.addAll(FXCollections.observableList(new Gson().fromJson(json, listType)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void saveToFile() {
        try {
            Files.writeString(configFilePath(), new Gson().toJson(this.connections));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
