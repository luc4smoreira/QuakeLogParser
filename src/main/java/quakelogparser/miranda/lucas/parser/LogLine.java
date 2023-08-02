package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;

public class LogLine {
    private String time;
    private LogEventTypeEnum type;
    private String rawData;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

}
