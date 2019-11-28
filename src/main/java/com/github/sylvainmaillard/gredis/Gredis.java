package com.github.sylvainmaillard.gredis;

import com.github.sylvainmaillard.gredis.application.ConnectionService;
import com.github.sylvainmaillard.gredis.gui.LogComponent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.sylvainmaillard.gredis.ServicesLocator.loadDependency;
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

    private ConnectionService connectionService;


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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        this.connectionService = loadDependency(ConnectionService.class);

        connectionPane.visibleProperty().bind(this.connectionService.connected);

        connectionPane.textProperty().bindBidirectional(this.connectionService.connected, new StringConverter<>() {
            @Override
            public String toString(Boolean aBoolean) {
                return bundle.getString("connected.title") + " tcp://" + connectionService.redisHost.get() + ":" + connectionService.redisPort.get();
            }

            @Override
            public Boolean fromString(String s) {
                return null;
            }
        });
    }

    public void displayKeys(ActionEvent actionEvent) {
        // bind la liste de clés sur la session:
        this.keyList.itemsProperty().bindBidirectional(this.connectionService.redisSession.keysProperty());
        // charge les clés
        this.connectionService.redisSession.keys();
    }
}
