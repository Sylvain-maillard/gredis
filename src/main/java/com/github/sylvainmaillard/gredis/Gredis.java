package com.github.sylvainmaillard.gredis;

import com.github.sylvainmaillard.gredis.gui.FXMLUtils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static com.github.sylvainmaillard.gredis.gui.FXMLUtils.*;

@SpringBootApplication
public class Gredis extends Application implements Initializable {

    private static final Logger logger = Logger.getLogger(Gredis.class.getName());

    public TitledPane connectionPane;
    public ListView<String> keyList;
    private ResourceBundle bundle;

    private ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Gredis.class);
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtils.loadResource(this), getLabelsBundle());
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void stop() {
        springContext.stop();
        logger.info("Stopped app");
    }

    @Override
    public void start(Stage stage) {

        try {
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/assets/gredis.css").toExternalForm());

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
//        connectionPane.visibleProperty().bind(this.connectionService.connected);

//        connectionPane.textProperty().bindBidirectional(this.connectionService.connected, new StringConverter<>() {
//            @Override
//            public String toString(Boolean aBoolean) {
//                return bundle.getString("connected.title") + " tcp://" + connectionService.redisHost.get() + ":" + connectionService.redisPort.get();
//            }
//
//            @Override
//            public Boolean fromString(String s) {
//                return null;
//            }
//        });
    }

    public void displayKeys(ActionEvent actionEvent) {
        // bind la liste de clés sur la session:
//        this.keyList.itemsProperty().bindBidirectional(this.connectionService.redisSession.keysProperty());
        // charge les clés
//        this.connectionService.redisSession.keys();
    }

    public static void main(String[] args) {
        launch();
    }
}
