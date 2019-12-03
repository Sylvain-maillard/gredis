package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.SavedConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.fromBundle;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadImage;

public class SavedConnectionDialog extends Dialog<SavedConnection> implements Initializable {

    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField authTextField;
    @FXML
    private TextField nameTextField;

    private final StringProperty host = new SimpleStringProperty();
    private final StringProperty port = new SimpleStringProperty();
    private final StringProperty auth = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();

    public SavedConnectionDialog() {

        DialogPane result;
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtils.loadResource(this), FXMLUtils.getLabelsBundle());
        fxmlLoader.setClassLoader(FXMLUtils.class.getClassLoader());
        fxmlLoader.setControllerFactory(param -> this);
        fxmlLoader.setRoot(new DialogPane());
        try {
            result = fxmlLoader.load();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
            throw new RuntimeException(exception);
        }
        setDialogPane(result);

        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                URI uri = URI.create("tcp://" + host.get() + ":" + port.get());
                return new SavedConnection(name.get(), uri, auth.get());
            }
            return null;
        });

        // Get the Stage.
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(loadImage("/assets/gredis.png"));
        stage.setTitle(fromBundle("app.title"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.host.bindBidirectional(this.hostTextField.textProperty());
        this.port.bindBidirectional(this.portTextField.textProperty());
        this.auth.bindBidirectional(this.authTextField.textProperty());
        this.name.bindBidirectional(this.nameTextField.textProperty());

        this.hostTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (this.name.isEmpty().get() && !newValue) {
                this.name.setValue(this.hostTextField.getText());
            }
        });
    }
}
