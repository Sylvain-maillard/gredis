package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.application.MainApplicationState;
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

    public MainApplicationState mainApplicationState;

    public ConnectionsPanelComponent() {
        loadFXMLResource(this);
        this.mainApplicationState = loadDependency(MainApplicationState.class);
    }

    public void connect() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        authTextBox.disableProperty().bind(this.connected);
//        hostTextBox.disableProperty().bind(this.connected);
//        portTextBox.disableProperty().bind(this.connected);
//
//        hostTextBox.textProperty().bindBidirectional(this.redisHost);
//        portTextBox.textProperty().bindBidirectional(this.redisPort);
//        authTextBox.textProperty().bindBidirectional(this.auth);
    }
}
