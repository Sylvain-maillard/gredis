package com.github.sylvainmaillard.gredis;

import com.github.sylvainmaillard.gredis.gui.LogComponent;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import redis.clients.jedis.Jedis;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static com.github.sylvainmaillard.gredis.Gredis.RedisSession.SessionState.*;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.getLabelsBundle;
import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.loadResource;

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
            ResourceBundle bundle = getLabelsBundle();
            FXMLLoader loader = new FXMLLoader(loadResource("Gredis.fxml"), bundle);
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image(loadResource("gredis.png").openStream()));
            stage.setTitle(bundle.getString("app.title"));
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
        RedisSession.SessionState state = redisSession.connect();
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
        this.keyList.itemsProperty().bindBidirectional(this.redisSession.keys);
        // charge les clés
        this.redisSession.keys();
    }

    static class RedisSession {

        private final LogComponent log;
        private final String auth;

        private final ListProperty<String> keys = new SimpleListProperty<>();

        public void keys() {
            keys.clear();
            log.logRequest("KEYS");
            try {
                Set<String> keys = jedis.keys("*");
                log.logResponse("found " + keys.size() + " keys");
                this.keys.setValue(FXCollections.observableArrayList(keys));
            } catch (Exception e) {
                log.logError(e);
            }
        }

        enum SessionState {
            NOT_CONNECTED,
            CONNECTED,
            ERROR
        }

        private final Jedis jedis;
        private SessionState state;

        RedisSession(LogComponent log, String host, int port, String auth) {
            this.log = log;
            this.auth = auth;
            this.jedis = new Jedis(host, port);
            state = NOT_CONNECTED;
        }

        public SessionState connect() {
            log.logRequest("Connect", jedis.getClient().getHost(), String.valueOf(jedis.getClient().getPort()));
            try {
                jedis.connect();
                log.logResponse("Connected");
                if (this.auth != null && this.auth.length() > 0) {
                    log.logRequest("AUTH", "*****");
                    String response = jedis.auth(this.auth);
                    log.logResponse(response);
                }
                state = CONNECTED;
            } catch (Exception e) {
                log.logError(e);
                state = ERROR;
            }
            return state;
        }

        public SessionState close() {
            log.logRequest("Close");
            jedis.close();
            log.logResponse("Disconnected");
            state = NOT_CONNECTED;
            return state;
        }
    }
}
