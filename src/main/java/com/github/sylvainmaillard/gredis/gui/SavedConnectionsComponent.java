package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.RedisSession;
import com.github.sylvainmaillard.gredis.domain.SavedConnection;
import com.github.sylvainmaillard.gredis.domain.SavedConnections;
import com.github.sylvainmaillard.gredis.domain.SessionState;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.domain.SessionState.CONNECTED;
import static com.github.sylvainmaillard.gredis.domain.SessionState.ERROR;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.alertError;
import static javafx.application.Platform.runLater;

@Component
public class SavedConnectionsComponent {

    private final SavedConnections savedConnections;
    private final RedisSession redisSession;
    private final MainPaneComponent mainPaneComponent;

    @FXML
    public ListView<SavedConnection> savedConnectionsListView;
    public Button removeButton;
    public Button connectButton;

    @FXML
    private ResourceBundle resources;

    public SavedConnectionsComponent(SavedConnections savedConnections, RedisSession redisSession, MainPaneComponent mainPaneComponent) {
        this.savedConnections = savedConnections;
        this.redisSession = redisSession;
        this.mainPaneComponent = mainPaneComponent;
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

        redisSession.stateProperty().addListener((observable, oldValue, newValue) -> runLater(() -> {
            if (newValue == CONNECTED) {
                connectButton.setText(resources.getString("connection.disconnect"));
                connectButton.setOnAction(this::disconnect);
            } else {
                connectButton.setText(resources.getString("connection.connect"));
                connectButton.setOnAction(this::connect);
            }
        }));
    }

    private void disconnect(ActionEvent actionEvent) {
        redisSession.close();
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

    public void connect(ActionEvent actionEvent) {
        SavedConnection selectedItem = savedConnectionsListView.getSelectionModel().getSelectedItem();
        Task<SessionState> connectTask = new Task<>() {
            @Override
            protected SessionState call() {
                updateMessage("Connecting...");
                SessionState state = redisSession.connect(selectedItem);
                if (state == ERROR) {
                    updateMessage("Error: " + state);
                    alertError("Cannot connect to " + selectedItem);
                    failed();
                }
                updateMessage("Connected: " + state);
                done();
                return state;
            }
        };

        mainPaneComponent.mainPane.disableProperty().bind(connectTask.runningProperty());

        connectTask.setOnFailed(event ->  alertError("Cannot connect to " + selectedItem));

        Thread th = new Thread(connectTask);
        th.setDaemon(true);
        th.start();
    }
}
