package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.parser.LogLine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    //there will be only one parameter it is the player id

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



                }
                case CLIENT_USERINFO_CHANGED -> {
                    //rawParamsTrim example:"2 n\Dono da Bola\t\0\model\sarge/krusade\hmodel\sarge/krusade\g_redteam\\g_blueteam\\c1\5\c2\5\hc\95\w\0\l\0\tt\0\tl\0"
                    String[] parts = rawParamsTrim.split(" ", 2);

                    if(parts.length > 1) {
                        int playerId = Integer.parseInt(parts[0]);

                        //it will get the pattern using regex between n\ and \
                        //O assume it will always be the user name

                        Pattern pattern = Pattern.compile("n\\\\(.*?)\\\\");
                        Matcher matcher = pattern.matcher(parts[1]);

                        if (matcher.find()) {
                            String playerName = matcher.group(1);
                            event = new ClientUserinfoChangedEvent(playerId, playerName);
                        }
                        else {
                            System.out.println("    -");
                        }
                    }
                    //else {
                        //nothing to change
                    //}
                }

                case ITEM -> {
                }
                case KILL -> {
                }
            }
        }


        //make sure the data from log line is defined.
        if(event!=null) {
            event.setType(logLine.getType());
            event.setTime(logLine.getTime());
            event.setRawData(logLine.getRawData());
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
