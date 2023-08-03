package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;

public class LogLine {
    private int timeInSeconds;
    private LogEventTypeEnum type;
    private String rawData;

    private int lineNumber;


    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public LogEventTypeEnum getType() {
        return type;
    }

    public void setType(LogEventTypeEnum type) {
        this.type = type;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

}
