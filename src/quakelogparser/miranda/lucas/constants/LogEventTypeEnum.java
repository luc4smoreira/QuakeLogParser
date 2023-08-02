package quakelogparser.miranda.lucas.constants;

public enum LogEventTypeEnum {
    INIT_GAME("InitGame"),
    SHUT_DOWN_GAME("ShutdownGame"),
    EXIT("Exit"),
    CLIENT_CONNECT("ClientConnect"),
    CLIENT_USERINFO_CHANGED("ClientUserinfoChanged"),
    CLIENT_BEGIN("ClientBegin"),
    CLIENT_DISCONNECT("ClientDisconnect"),
    ITEM("Item"),

    KILL("Kill");


    private String value;
    LogEventTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
