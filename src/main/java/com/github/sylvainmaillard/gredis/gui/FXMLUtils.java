package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.Gredis;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class FXMLUtils {

    public static Image loadImage(String classpath) {
        return new Image(loadResourceOpenStream(classpath));
    }

    public static String fromBundle(String key) {
        return getLabelsBundle().getString(key);
    }

    public static <T> T loadFXMLResource(Object node) {
        FXMLLoader fxmlLoader = new FXMLLoader(loadResource(node), getLabelsBundle());

        if (node instanceof Node) {
            fxmlLoader.setRoot(node);
            fxmlLoader.setControllerFactory(param -> node);
        }

        try {
            return fxmlLoader.load();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private static InputStream loadResourceOpenStream(String classpath) {
        try {
            return loadResource(classpath).openStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static URL loadResource(String classpath) {

        URL resource = Thread.currentThread().getContextClassLoader().getResource(classpath);
        if (resource == null) {
            resource = Gredis.class.getResource(classpath);
            if (resource == null) {
                throw new IllegalArgumentException("classpath " + classpath + " resource can not be found.");
            }
        }
        return requireNonNull(resource, "Could not find resource " + classpath);
    }

    public static ResourceBundle getLabelsBundle() {
        return ResourceBundle.getBundle("gui.labels", Locale.getDefault());
    }

    public static URL loadResource(Object customComponent) {
        Class<?> componentClass = requireNonNull(customComponent).getClass();
        return loadResource("/" + componentClass.getPackageName().replace(".", "/") + "/" + componentClass.getSimpleName() + ".fxml");
    }
}
