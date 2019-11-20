module gredis {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.sylvainmaillard.gredis to javafx.fxml;
    exports com.github.sylvainmaillard.gredis;
}