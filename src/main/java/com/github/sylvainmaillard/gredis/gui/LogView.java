package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.domain.logs.LogLine;
import com.github.sylvainmaillard.gredis.domain.logs.Logs;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

import static javafx.application.Platform.runLater;

@Component
public class LogView extends HBox {

    public ScrollPane logContainer;
    public TextFlow logTextArea;

    private static final Logger logger = Logger.getLogger(LogView.class.getName());
    private final Logs logs;

    public LogView(Logs logs) {
        this.logs = logs;
    }

    @PostConstruct
    public void init() {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(this);
        loader.setLocation(FXMLUtils.loadResource(this));
        loader.setResources(FXMLUtils.getLabelsBundle());
        loader.setControllerFactory(param -> this);
        loader.setClassLoader(this.getClass().getClassLoader());

        try {
            final Node root = loader.load();
            assert root == this;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void initialize() {
        logs.addNewLogsMessagesListener(this::log);

        logTextArea.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    logTextArea.layout();
                    logContainer.layout();
                    logContainer.setVvalue(1.0f);
                }));
    }

    private void log(LogLine message) {
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

    private void log(LogLine msg, LogStyle style) {
        runLater(() -> logTextArea.getChildren().add(style.getText(msg.toString())));
    }

    public void clear(ActionEvent actionEvent) {
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
