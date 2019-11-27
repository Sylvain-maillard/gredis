package com.github.sylvainmaillard.gredis.domain;

import com.github.sylvainmaillard.gredis.application.LogService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static com.github.sylvainmaillard.gredis.domain.SessionState.*;

public class RedisSession {

    private final LogService log;
    private final String auth;

    private final ListProperty<String> keys = new SimpleListProperty<>();

    public ListProperty<String> keysProperty() {
        return keys;
    }

    public void keys() {
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

    private final Jedis jedis;
    private SessionState state;

    public RedisSession(LogService log, String host, int port, String auth) {
        this.log = log;
        this.auth = auth;
        this.jedis = new Jedis(host, port);
        state = NOT_CONNECTED;
    }

    public SessionState connect() {
        log.logRequest("Connect", jedis.getClient().getHost(), String.valueOf(jedis.getClient().getPort()));
        try {
            jedis.connect();
            log.logResponse("Connected");
            if (this.auth != null && this.auth.length() > 0) {
                log.logRequest("AUTH", "*****");
                String response = jedis.auth(this.auth);
                log.logResponse(response);
            }
            state = CONNECTED;
        } catch (Exception e) {
            log.logError(e);
            state = ERROR;
        }
        return state;
    }

    public void close() {
        log.logRequest("Close");
        jedis.close();
        log.logResponse("Disconnected");
        state = NOT_CONNECTED;
    }
}
