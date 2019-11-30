package com.github.sylvainmaillard.gredis.domain.logs;

public abstract class LogLine {
    private final LogType type;

    LogLine(LogType logType) {
        this.type = logType;
    }

    public LogType getType() {
        return type;
    }
}
