module gredis {
    uses com.github.sylvainmaillard.gredis.application.MainApplicationState;
    provides com.github.sylvainmaillard.gredis.application.MainApplicationState with com.github.sylvainmaillard.gredis.application.MainApplicationState;

    requires javafx.controls;
    requires javafx.fxml;

    requires jedis;

    opens com.github.sylvainmaillard.gredis.gui to javafx.fxml;
    opens com.github.sylvainmaillard.gredis to javafx.fxml;
    exports com.github.sylvainmaillard.gredis;
}
