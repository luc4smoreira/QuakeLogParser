package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.Main;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;
import quakelogparser.miranda.lucas.service.GameService;
import quakelogparser.miranda.lucas.service.GameServiceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuakeLogParserImp implements QuakeLogParser {


    private Map<String, LogEventTypeEnum> allowedEventTypes;


    public QuakeLogParserImp() {
        allowedEventTypes = new HashMap<>();

        for(LogEventTypeEnum eventType : LogEventTypeEnum.values()) {
            allowedEventTypes.put(eventType.getValue(), eventType);
        }

    }

    public GameService parseFile(final String fileName) {

        GameService gameService = new GameServiceImp();

        //open the file and prepare to read it
        InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(inputStreamReader);

        try (br) {
            String rawLine = br.readLine();


            //read each line, one by one, until the end
            for(int line = 1; rawLine != null; line++) {

                try {
                    rawLine = rawLine.trim(); //remove spaces at beginning and end

                    if(!rawLine.isEmpty()) {

                        LogLine logLine = parseLine(rawLine);

                        if(logLine!=null && logLine.getType()!=null) {
                            switch (logLine.getType()) {
                                case INIT_GAME -> {
                                    gameService.initGame();
                                }
                                case SHUT_DOWN_GAME -> {
                                    gameService.shutDownGame();
                                }
                                case EXIT -> {
                                    //do nothing
                                }
                                case CLIENT_CONNECT -> {

                                }
                                case CLIENT_USERINFO_CHANGED -> {
                                }
                                case CLIENT_BEGIN -> {
                                }
                                case ITEM -> {
                                    //do nothing
                                }
                                case KILL -> {
//                                try {
//                                    int killer = getParamAsInteger(logLine.getEventParam(0));
//                                    int victim = getParamAsInteger(logLine.getEventParam(1));
//                                    int meansOfDeath = getParamAsInteger(logLine.getEventParam(2));
//                                    gameService.playerKill(killer, victim, meansOfDeath);
//                                }
//                                catch (NumberFormatException e) {
//                                    //TODO
//                                }
//                                catch (PlayerDoesntExist | PlayerIsNotInTheGame e) {
//                                    //TODO
//                                }

                                }
                            }
                        }

                    }
                }
                catch (CorruptedLogLine e) {
                    String message = String.format("Warning: Line %d looks corrupted. %s", line, e.getMessage());
                    System.out.println(message);
                }
                rawLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameService;
    }

    private int getParamAsInteger(String param) {
        return Integer.parseInt(param);
    }

    private LogLine parseLine(String rawLine) throws CorruptedLogLine {
        LogLine logLine = null;

        String[] parts = rawLine.split(" ", 3);

        if (parts.length >= 2) {
            String time = parts[0]; // time

            if(time.contains(":")) {
                logLine = new LogLine();
                logLine.setRawData(rawLine);

                logLine.setTime(time);
                String eventType = parts[1]; // event type


                LogEventTypeEnum logEventTypeEnum = readEventType(eventType);



                //It can be just a line with -------
                if(logEventTypeEnum!=null) {
                    logLine.setType(logEventTypeEnum);

//                    //if it has params
//                    if (parts.length == 3) {
//                        List<String> params = getEventParams(parts[2], logEventTypeEnum);
//
//                        for (String param : params) {
//                            logLine.addEventParam(param);
//                        }
//                    }
                }


            }
            else {
                //invalid log, time invalid
                throw new CorruptedLogLine("Invalid log line, time invalid: "+rawLine);
            }
        }
        else {
            //data incomplete, only time? doesnt matter
            throw new CorruptedLogLine("Invalid log line, incomplete data: "+rawLine);
        }
        return logLine;
    }

    private LogEventTypeEnum readEventType(String eventType) {
        LogEventTypeEnum type;
        String eventName = eventType.replace(":", "");

        //linear search, it´s not the best FIXME
        type = allowedEventTypes.get(eventName);

        return type;
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
