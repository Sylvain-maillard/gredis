package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.application.ConnectionService;
import com.github.sylvainmaillard.gredis.domain.SavedConnections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadFXMLResource;

public class SavedConnectionDialogPane extends DialogPane implements Initializable {

    public Button connectBtn;
    public PasswordField authTextBox;
    public TextField portTextBox;
    public TextField hostTextBox;

    private ConnectionService connectionService;
    private SavedConnections savedConnections;

    public SavedConnectionDialogPane() {
        loadFXMLResource(this);
    }

    public void connect(ActionEvent event) {
        // ouvre une connection.
        this.connectionService.connect();
    }

    @Override
    public void initialize(URL location, ResourceBundle bundle) {
//        this.connectionService = loadDependency(ConnectionService.class);
//        this.savedConnections = loadDependency(SavedConnections.class);
//
//        authTextBox.disableProperty().bind(this.connectionService.connected);
//        hostTextBox.disableProperty().bind(this.connectionService.connected);
//        portTextBox.disableProperty().bind(this.connectionService.connected);
//
//        hostTextBox.textProperty().bindBidirectional(this.connectionService.redisHost);
//        portTextBox.textProperty().bindBidirectional(this.connectionService.redisPort);
//        authTextBox.textProperty().bindBidirectional(this.connectionService.auth);
//
//        connectionService.connected.addListener((observable, oldValue, newValue) -> {
//            if (newValue) {
//                connectBtn.setText(bundle.getString("connection.disconnect"));
//                connectBtn.setOnAction(this::disconnect);
//            } else {
//                connectBtn.setText(bundle.getString("connection.connect"));
//                connectBtn.setOnAction(this::connect);
//            }
//        });
    }

    private void disconnect(ActionEvent actionEvent) {
        this.connectionService.disconnect();
    }
}
