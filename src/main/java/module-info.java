module gredis {

    uses com.github.sylvainmaillard.gredis.application.ConnectionService;
    uses com.github.sylvainmaillard.gredis.application.LogService;
    uses com.github.sylvainmaillard.gredis.domain.SavedConnections;

    provides com.github.sylvainmaillard.gredis.domain.SavedConnections with com.github.sylvainmaillard.gredis.infrastructure.FileSavedConnections;
    provides com.github.sylvainmaillard.gredis.application.LogService with com.github.sylvainmaillard.gredis.application.LogService;
    provides com.github.sylvainmaillard.gredis.application.ConnectionService with com.github.sylvainmaillard.gredis.application.ConnectionService;

    requires javafx.controls;
    requires javafx.fxml;

    requires jedis;

    requires com.google.gson;

    opens com.github.sylvainmaillard.gredis.gui to javafx.fxml;
    opens com.github.sylvainmaillard.gredis to javafx.fxml;
    exports com.github.sylvainmaillard.gredis;
}
