package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.application.LogService;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadDependency;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadFXMLResource;
import static javafx.application.Platform.runLater;

public class LogComponent extends HBox implements Initializable {
    public ScrollPane logContainer;
    public TextFlow logTextArea;

    private LogService logService;

    public LogComponent() {
       loadFXMLResource(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.logService = loadDependency(LogService.class);

        logService.addNewLogsMessagesListener(this::log);

        logTextArea.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    logTextArea.layout();
                    logContainer.layout();
                    logContainer.setVvalue(1.0f);
                }));
    }

    private void log(LogService.LogLine message) {
        switch (message.getType()) {
            case REQUEST:
                log(message, LogStyle.REQUEST);
                break;
            case RESPONSE:
                log(message, LogStyle.RESPONSE);
                break;
            case ERROR:
                log(message, LogStyle.ERROR);
                break;
        }
    }

    private void log(LogService.LogLine msg, LogStyle style) {
        runLater(() -> logTextArea.getChildren().add(style.getText(msg.toString())));
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
