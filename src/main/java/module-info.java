module gredis {

    uses com.github.sylvainmaillard.gredis.application.ConnectionService;

    provides com.github.sylvainmaillard.gredis.application.ConnectionService with com.github.sylvainmaillard.gredis.application.ConnectionService;

    requires javafx.controls;
    requires javafx.fxml;

    requires jedis;

    requires com.google.gson;

    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.beans;
    requires java.annotation;

    opens com.github.sylvainmaillard.gredis.infrastructure to spring.beans;
    opens com.github.sylvainmaillard.gredis.domain.logs to spring.beans;
    opens com.github.sylvainmaillard.gredis.domain to spring.beans, com.google.gson;
    opens com.github.sylvainmaillard.gredis.gui to javafx.fxml, spring.beans;
    opens com.github.sylvainmaillard.gredis to javafx.fxml, spring.core;
    exports com.github.sylvainmaillard.gredis;
}
