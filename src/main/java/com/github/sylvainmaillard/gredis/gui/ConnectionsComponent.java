package com.github.sylvainmaillard.gredis.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadImage;

@Component
public class ConnectionsComponent {

    @FXML
    private ResourceBundle resources;

    public void add(ActionEvent actionEvent) {

        Dialog dialog = new Dialog();
        dialog.setDialogPane(new SavedConnectionDialogPane());
        // Get the Stage.
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(loadImage("/assets/gredis.png"));
        stage.setTitle(resources.getString("app.title"));

        dialog.showAndWait();
    }
}
