package com.github.sylvainmaillard.gredis.gui;

import com.github.sylvainmaillard.gredis.Gredis;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class FXMLUtils {

    public static URL loadResource(String classpath) {

        URL resource = Thread.currentThread().getContextClassLoader().getResource(classpath);
        if (resource == null) {
            resource = Gredis.class.getResource(classpath);
            if (resource == null) {
                throw new IllegalArgumentException("classpath " + classpath + " resource can not be found.");
            }
        }
        return requireNonNull(resource, "Could not find resource " + classpath);
    }

    public static URL loadResource(Object customComponent) {
        Class<?> componentClass = requireNonNull(customComponent).getClass();
        return  loadResource("/"+ componentClass.getPackageName().replace(".","/") + "/"+ componentClass.getSimpleName() + ".fxml");
    }

    public static ResourceBundle getLabelsBundle() {
        return ResourceBundle.getBundle("gui.labels", Locale.getDefault());
    }
}
