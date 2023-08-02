package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class LogLine {
    private String time;
    private LogEventTypeEnum type;
    private String rawData;

    private List<String> eventParams;

    public LogLine() {
        eventParams = new ArrayList<>();
    }

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

    public void addEventParam(String param) {
        eventParams.add(param);
    }

    public String getEventParam(int index) {
        return eventParams.get(index);
    }
}
