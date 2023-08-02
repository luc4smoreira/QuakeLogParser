package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.parser.LogLine;

import java.util.ArrayList;
import java.util.List;

public class EventsFactory {

    public static LogLine createEvent(LogLine logLine, String rawParams) {
        LogLine event = logLine;

        String rawParamsTrim = rawParams.trim();

        if(logLine.getType()!=null) {

            switch (logLine.getType()) {
                case INIT_GAME -> {
                }
                case SHUT_DOWN_GAME -> {
                }
                case EXIT -> {
                }
                case CLIENT_CONNECT, CLIENT_BEGIN, CLIENT_DISCONNECT-> {


                    int playerId = Integer.parseInt(rawParamsTrim);

                    if(logLine.getType().equals(LogEventTypeEnum.CLIENT_CONNECT)) {
                        event = new ClientConnectEvent(playerId);
                    }
                    else if(logLine.getType().equals(LogEventTypeEnum.CLIENT_BEGIN)) {
                        event = new ClientBeginEvent(playerId);
                    }
                    else {
                        event = new ClientDisconnectEvent(playerId);
                    }

                    event.setType(logLine.getType());
                    event.setTime(logLine.getTime());
                    event.setRawData(logLine.getRawData());

                }
                case CLIENT_USERINFO_CHANGED -> {
                }

                case ITEM -> {
                }
                case KILL -> {
                }
            }
        }



        return event;
    }



    private List<String> getEventParams(String rawParams, LogEventTypeEnum typeEnum) {

        List<String> params = new ArrayList<>();


//        if(typeEnum.equals(LogEventTypeEnum.KILL)) {
//            String[] parts = rawParams.split(":", 2);
//
//            String part = parts[0].trim();
//            part.split(" ")
//
//        }

        return params;

    }

}
