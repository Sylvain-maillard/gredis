package com.github.sylvainmaillard.gredis.domain;

import com.github.sylvainmaillard.gredis.domain.logs.Logs;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static com.github.sylvainmaillard.gredis.domain.SessionState.*;

@Component
public class RedisSession {

    private final Logs log;

    private final ObjectProperty<SessionState> state = new SimpleObjectProperty<>();

    private final ListProperty<String> keys = new SimpleListProperty<>();

    public ListProperty<String> keysProperty() {
        return keys;
    }

    public void keys() {
        assert state.get() == CONNECTED;
        keys.clear();
        log.logRequest("KEYS");
        try {
            Set<String> keys = jedis.keys("*");
            log.logResponse("found " + keys.size() + " keys");
            this.keys.setValue(FXCollections.observableArrayList(keys));
        } catch (Exception e) {
            log.logError(e);
        }
    }

    private Jedis jedis;

    public RedisSession(Logs log) {
        this.log = log;
        state.setValue(NOT_CONNECTED);
    }

    public SessionState connect(SavedConnection savedConnection) {
        jedis = new Jedis(savedConnection.getUri());
        log.logRequest("Connect", jedis.getClient().getHost(), String.valueOf(jedis.getClient().getPort()));
        try {
            jedis.connect();
            log.logResponse("Connected");
            if (savedConnection.hasAuth()) {
                log.logRequest("AUTH", "*****");
                String response = jedis.auth(savedConnection.getAuth());
                log.logResponse(response);
            }
            state.setValue(CONNECTED);
        } catch (Exception e) {
            log.logError(e);
            state.setValue(ERROR);
        }
        return state.get();
    }

    public void close() {
        log.logRequest("Close");
        jedis.close();
        log.logResponse("Disconnected");
        state.setValue(NOT_CONNECTED);
    }

    public SessionState getState() {
        return state.get();
    }

    public ObjectProperty<SessionState> stateProperty() {
        return state;
    }
}
