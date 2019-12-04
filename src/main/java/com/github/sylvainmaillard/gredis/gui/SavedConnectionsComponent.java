package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.SavedConnection;
import com.github.sylvainmaillard.gredis.domain.SavedConnections;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.alertError;
import static javafx.application.Platform.runLater;

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
        removeButton.disableProperty().bind(Bindings.isEmpty(savedConnectionsListView.getSelectionModel().getSelectedItems()));
        savedConnectionsListView.itemsProperty().bindBidirectional(savedConnections.savedConnectionsProperty());
        savedConnectionsListView.setCellFactory(cell -> new ListCell<>() {

            final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(SavedConnection cnx, boolean empty) {
                super.updateItem(cnx, empty);

                if (cnx == null || empty) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(cnx.getName());

                    // Let's show our Author when the user hovers the mouse cursor over this row
                    tooltip.setText(cnx.getUri());
                    setTooltip(tooltip);
                }
            }
        });
    }

    public void add(ActionEvent actionEvent) {
        SavedConnectionDialog dialog = new SavedConnectionDialog();
        dialog.showAndWait()
                .filter(savedConnection -> savedConnections.doesNotContains(savedConnection))
                .ifPresentOrElse(savedConnection -> runLater(() -> savedConnections.add(savedConnection)), () -> alertError(resources.getString("connections.already_exists")));
    }

    public void edit(ActionEvent actionEvent) {
        SavedConnectionDialog dialog = new SavedConnectionDialog();
        SavedConnection selectedItem = savedConnectionsListView.getSelectionModel().getSelectedItem();
        dialog.prepareWith(selectedItem);
        dialog.showAndWait().ifPresent(cnx ->  runLater(() -> savedConnections.replace(selectedItem, cnx)));
    }

    public void remove(ActionEvent actionEvent) {
        runLater(() -> savedConnections.remove(savedConnectionsListView.getSelectionModel().getSelectedItem()));
    }
}
