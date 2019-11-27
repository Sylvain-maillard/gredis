package com.github.sylvainmaillard.gredis;

import com.github.sylvainmaillard.gredis.domain.RedisSession;
import com.github.sylvainmaillard.gredis.domain.SessionState;
import com.github.sylvainmaillard.gredis.gui.LogComponent;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.domain.SessionState.*;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.*;

public class Gredis extends Application implements Initializable {

    public Button connectBtn;
    public PasswordField authTextBox;
    public TextField portTextBox;
    public TextField hostTextBox;
    public TitledPane connectionPane;
    public SplitPane contentSplitPane;
    public LogComponent log;
    public ListView<String> keyList;
    private ResourceBundle bundle;

    private BooleanProperty connected = new SimpleBooleanProperty(false);
    private StringProperty redisHost = new SimpleStringProperty("NEIVE");
    private StringProperty redisPort = new SimpleStringProperty("50301");
    private RedisSession redisSession;
    private StringProperty auth = new SimpleStringProperty("jesuisunmotdepassecomplexe");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        try {
            AnchorPane root = loadFXMLResource(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(loadImage("/assets/gredis.png"));
            stage.setTitle(fromBundle("app.title"));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    public void connect(ActionEvent actionEvent) {
        // ouvre une connection.
        connected.setValue(true);

        redisSession = new RedisSession(log, redisHost.get(), Integer.parseInt(redisPort.get()), auth.get());
        SessionState state = redisSession.connect();
        if (state == ERROR) {
            connected.setValue(false);
        }
    }

    private void disconnect(ActionEvent actionEvent) {
        redisSession.close();
        connected.setValue(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;

        connectionPane.visibleProperty().bind(this.connected);

        connectionPane.textProperty().bindBidirectional(this.connected, new StringConverter<Boolean>() {
            @Override
            public String toString(Boolean aBoolean) {
                return bundle.getString("connected.title") + " tcp://" + redisHost.get() + ":" + redisPort.get();
            }

            @Override
            public Boolean fromString(String s) {
                return null;
            }
        });

        authTextBox.disableProperty().bind(this.connected);
        hostTextBox.disableProperty().bind(this.connected);
        portTextBox.disableProperty().bind(this.connected);

        hostTextBox.textProperty().bindBidirectional(this.redisHost);
        portTextBox.textProperty().bindBidirectional(this.redisPort);
        authTextBox.textProperty().bindBidirectional(this.auth);

        connected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                connectBtn.setText(bundle.getString("connection.disconnect"));
                connectBtn.setOnAction(this::disconnect);
            } else {
                connectBtn.setText(bundle.getString("connection.connect"));
                connectBtn.setOnAction(this::connect);
            }
        });
    }

    public void displayKeys(ActionEvent actionEvent) {
        // bind la liste de clés sur la session:
        this.keyList.itemsProperty().bindBidirectional(this.redisSession.keysProperty());
        // charge les clés
        this.redisSession.keys();
    }
}
