module gredis {

    requires javafx.controls;
    requires javafx.fxml;

    requires jedis;

    opens com.github.sylvainmaillard.gredis.gui to javafx.fxml;
    opens com.github.sylvainmaillard.gredis to javafx.fxml;
    exports com.github.sylvainmaillard.gredis;
}
