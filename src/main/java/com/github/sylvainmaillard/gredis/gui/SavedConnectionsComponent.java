package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.SavedConnection;
import com.github.sylvainmaillard.gredis.domain.SavedConnections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class SavedConnectionsComponent {

    private final SavedConnections savedConnections;

    @FXML
    public ListView<SavedConnection> savedConnectionsListView;
    public Button removeButton;

    @FXML
    private ResourceBundle resources;

    public SavedConnectionsComponent(SavedConnections savedConnections) {
        this.savedConnections = savedConnections;
    }

    public void initialize() {
        removeButton.disableProperty().bindBidirectional(savedConnections.emptynessProperty());
        savedConnectionsListView.itemsProperty().bindBidirectional(savedConnections.savedConnectionsProperty());
    }

    public void add(ActionEvent actionEvent) {
        SavedConnectionDialog dialog = new SavedConnectionDialog();
        dialog.showAndWait().ifPresent(savedConnections::add);
    }

    public void edit(ActionEvent actionEvent) {

    }

    public void remove(ActionEvent actionEvent) {
        savedConnections.remove(savedConnectionsListView.getSelectionModel().getSelectedItem());
    }
}
