package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.application.MainApplicationState;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadDependency;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadFXMLResource;

public class ConnectionsPanelComponent extends HBox implements Initializable {

    public Button connectBtn;
    public PasswordField authTextBox;
    public TextField portTextBox;
    public TextField hostTextBox;

    private MainApplicationState mainApplicationState;

    public ConnectionsPanelComponent() {
        loadFXMLResource(this);

    }

    public void connect(ActionEvent event) {
        // ouvre une connection.
        this.mainApplicationState.connect();
    }

    @Override
    public void initialize(URL location, ResourceBundle bundle) {
        this.mainApplicationState = loadDependency(MainApplicationState.class);
        authTextBox.disableProperty().bind(this.mainApplicationState.connected);
        hostTextBox.disableProperty().bind(this.mainApplicationState.connected);
        portTextBox.disableProperty().bind(this.mainApplicationState.connected);

        hostTextBox.textProperty().bindBidirectional(this.mainApplicationState.redisHost);
        portTextBox.textProperty().bindBidirectional(this.mainApplicationState.redisPort);
        authTextBox.textProperty().bindBidirectional(this.mainApplicationState.auth);

        mainApplicationState.connected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                connectBtn.setText(bundle.getString("connection.disconnect"));
                connectBtn.setOnAction(this::disconnect);
            } else {
                connectBtn.setText(bundle.getString("connection.connect"));
                connectBtn.setOnAction(this::connect);
            }
        });
    }

    private void disconnect(ActionEvent actionEvent) {
        this.mainApplicationState.disconnect();
    }
}
