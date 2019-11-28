package com.github.sylvainmaillard.gredis.application;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class LogService {

    private ObservableList<LogLine> logMessages = FXCollections.observableArrayList();

    public void logRequest(String request, String...args) {
        logMessages.add(new RequestLine(request, args));
    }

    public void logResponse(String s) {
        logMessages.add(new ResponseLine(s));
    }

    public void logError(Exception e) {
        logMessages.add(new ErrorLine(e));
    }

    @FunctionalInterface
    public interface NewLogMessageListener {
        void onNewMessage(LogLine message);
    }

    public void addNewLogsMessagesListener(NewLogMessageListener messageListener) {
        logMessages.addListener((ListChangeListener<LogLine>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(messageListener::onNewMessage);
                }
            }
        });
    }

    public enum LogType {
        REQUEST, RESPONSE, ERROR
    }

    public static abstract class LogLine {
        final LogType type;

        LogLine(LogType logType) {
            this.type = logType;
        }

        public LogType getType() {
            return type;
        }
    }

    public static class ErrorLine extends LogLine {
        private final Exception exception;

        ErrorLine(Exception exception) {
            super(LogType.ERROR);
            this.exception = exception;
        }
        public String toString() {
            return exception.getMessage();
        }
    }

    public static class RequestLine extends LogLine {
        private final String request;
        private final String[] args;

        RequestLine(String request, String... args) {
            super(LogType.REQUEST);

            this.request = request;
            this.args = args;
        }
        public String toString() {
            return request + " (" + String.join(", ", args) + ")";
        }
    }

    public static class ResponseLine extends LogLine {
        private final String response;

        ResponseLine(String response) {
            super(LogType.RESPONSE);
            this.response = response;
        }
        public String toString() {
            return response;
        }
    }

}
