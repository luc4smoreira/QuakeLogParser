package quakelogparser.miranda.lucas.constants;

/**
 * Defines the type of each event and also the priority to process this event in a group of events at the same time.
 */
public enum LogEventTypeEnum {

    INIT_GAME("InitGame", 0),
    CLIENT_CONNECT("ClientConnect", 10),
    CLIENT_BEGIN("ClientBegin", 20),
    CLIENT_USERINFO_CHANGED("ClientUserinfoChanged", 30),
    ITEM("Item", 40),
    KILL("Kill", 50),
    CLIENT_DISCONNECT("ClientDisconnect", 90),

    EXIT("Exit", 95),
    SHUT_DOWN_GAME("ShutdownGame", 100);








    private String value;
    private int priority;
    LogEventTypeEnum(String value, int priority) {
        this.value = value;
        this.priority = priority;

    }

    public String getValue() {
        return value;
    }

    /**
     * Get the priority to process this log in a group with the same time
     *
     * @return 0 for the most important, higher less important
     */
    public int getPriority() {
        return priority;
    }
}
