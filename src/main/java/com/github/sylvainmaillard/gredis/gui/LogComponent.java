package com.github.sylvainmaillard.gredis.gui;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.getLabelsBundle;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadResource;
import static javafx.application.Platform.runLater;

public class LogComponent extends HBox implements Initializable {
    public ScrollPane logContainer;
    public TextFlow logTextArea;

    public LogComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(loadResource(this), getLabelsBundle());
        fxmlLoader.setRoot(this);
        fxmlLoader.setControllerFactory(param -> this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        logTextArea.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    logTextArea.layout();
                    logContainer.layout();
                    logContainer.setVvalue(1.0f);
                }));
    }

    public void logRequest(String command, String... args) {
        log(command + " (" + String.join(", ", args) + ")", LogStyle.REQUEST);
    }

    public void logResponse(String msg) {
        log(msg, LogStyle.RESPONSE);
    }

    public void logError(Exception e) {
        log(e.getMessage(), LogStyle.ERROR);
    }

    private void log(String msg, LogStyle style) {
        runLater(() -> logTextArea.getChildren().add(style.getText(msg)));
    }

    @FXML
    private void clear() {
        runLater(() ->  logTextArea.getChildren().clear());
    }

    enum LogStyle {
        REQUEST {
            @Override
            Text getText(String msg) {
                Text t = new Text();
                t.setFill(Color.BLUE);
                t.setText("> " + msg + "\n");
                return t;
            }
        },
        RESPONSE {
            @Override
            Text getText(String msg) {
                Text t = new Text();
                t.setStyle("-fx-font-weight:bold;");
                t.setText("< " + msg + "\n");
                return t;
            }
        }, ERROR {
            @Override
            Text getText(String msg) {
                Text t = new Text();
                t.setFill(Color.RED);
                t.setStyle("-fx-font-weight:bold;-fx-font-size: 14");
                t.setText("< " + msg + "\n");
                return t;
            }
        };

        abstract Text getText(String msg);
    }
}
