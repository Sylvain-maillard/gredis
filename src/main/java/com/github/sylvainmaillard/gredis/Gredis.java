package com.github.sylvainmaillard.gredis;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Gredis extends Application implements Initializable{

    public Button connectBtn;
    public PasswordField authTextBox;
    public TextField portTextBox;
    public TextField hostTextBox;
    public TitledPane connectionPane;
    private ResourceBundle bundle;

    private BooleanProperty connected = new SimpleBooleanProperty(false);
    private StringProperty redisHost = new SimpleStringProperty("localhost");
    private StringProperty redisPort = new SimpleStringProperty("6379");

    @Override
    public void start(Stage stage) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("gui.labels", Locale.getDefault());
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

    public static void main(String[] args) {
        launch();
    }

    public void connect(ActionEvent actionEvent) {
        // ouvre une connection.
        connected.setValue(true);

        connectBtn.setText(bundle.getString("connection.disconnect"));
        connectBtn.setOnAction(this::disconnect);
    }

    private void disconnect(ActionEvent actionEvent) {
        connected.setValue(false);
        connectBtn.setText(bundle.getString("connection.connect"));
        connectBtn.setOnAction(this::connect);
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
    }

    private static URL loadResource(String classpath) {

        URL resource = Thread.currentThread().getContextClassLoader().getResource(classpath);
        if (resource == null) {
            resource = Gredis.class.getResource(classpath);
            if (resource == null) {
                throw new IllegalArgumentException("classpath " + classpath + " resource can not be found.");
            }
        }
        return resource;
    }
}
