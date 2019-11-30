package com.github.sylvainmaillard.gredis.application;


import com.github.sylvainmaillard.gredis.domain.RedisSession;
import com.github.sylvainmaillard.gredis.domain.SessionState;
import com.github.sylvainmaillard.gredis.domain.logs.Logs;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static com.github.sylvainmaillard.gredis.ServicesLocator.loadDependency;
import static com.github.sylvainmaillard.gredis.domain.SessionState.ERROR;

public class ConnectionService {

    private final Logs logs;
    public BooleanProperty connected = new SimpleBooleanProperty(false);
    public StringProperty redisHost = new SimpleStringProperty("NEIVE");
    public StringProperty redisPort = new SimpleStringProperty("50301");
    public RedisSession redisSession;
    public StringProperty auth = new SimpleStringProperty("jesuisunmotdepassecomplexe");

    public ConnectionService() {
        this.logs = loadDependency(Logs.class);
    }

    public void connect() {
        this.connected.setValue(true);

        this.redisSession = new RedisSession(logs, redisHost.get(), Integer.parseInt(redisPort.get()), auth.get());
        SessionState state = redisSession.connect();
        if (state == ERROR) {
            connected.setValue(false);
        }
    }

    public void disconnect() {
        redisSession.close();
        connected.setValue(false);
    }
}
