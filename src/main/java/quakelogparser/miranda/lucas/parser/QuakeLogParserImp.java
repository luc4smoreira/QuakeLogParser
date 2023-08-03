package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.Main;
import quakelogparser.miranda.lucas.constants.GameConstantValues;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.events.*;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;
import quakelogparser.miranda.lucas.service.GameService;
import quakelogparser.miranda.lucas.service.GameServiceImp;
import quakelogparser.miranda.lucas.service.RankingService;
import quakelogparser.miranda.lucas.service.RankingServiceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class QuakeLogParserImp implements QuakeLogParser {


    private Map<String, LogEventTypeEnum> allowedEventTypes;

    private RankingService rankingService;

    public QuakeLogParserImp() {
        allowedEventTypes = new HashMap<>();

        for(LogEventTypeEnum eventType : LogEventTypeEnum.values()) {
            allowedEventTypes.put(eventType.getValue(), eventType);
        }

        rankingService = new RankingServiceImp();

    }

    public GameService parseFile(final String fileName) {

        GameService gameService = new GameServiceImp();


        //open the file and prepare to read it
        InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(inputStreamReader);

        try (br) {
            String rawLine = br.readLine();



            //it is necessary to group logs by time, and process they ordering by event type
            // because it is possible to have this scenario:

            //0:04 Kill: 5 3 18: Chessus killed Dono da Bola by MOD_TELEFRAG
            //0:04 ClientBegin: 5 #it shouldn't be possible to kill someone not int the game
           List<LogLine> batchLogsByTime = new ArrayList<>();
           int timeLogsBatch = 0;


            //read each line, one by one, until the end
            for(int lineNumber = 1; rawLine != null; lineNumber++) {

                try {
                    rawLine = rawLine.trim(); //remove spaces at beginning and end
                    if(!rawLine.isEmpty()) {

                        LogLine logLine = parseLine(rawLine);

                        if(logLine!=null && logLine.getType()!=null) {

                            logLine.setLineNumber(lineNumber);

                            //if is the same time, add to the list to order the list by event type
                            if(timeLogsBatch == logLine.getTimeInSeconds()) {
                                batchLogsByTime.add(logLine);
                            }
                            else {
                                processLogsSameTime(batchLogsByTime, gameService);

                                batchLogsByTime.clear(); //clear the list
                                batchLogsByTime.add(logLine); //add the log to the next batch
                                timeLogsBatch = logLine.getTimeInSeconds(); //update the time associeated with the next batch

                            }

                        }

                    }
                }
                catch (CorruptedLogLine e) {
                    String message = String.format("Warning: Line %d looks corrupted. %s", lineNumber, e.getMessage());
                    System.out.println(message);
                }

                rawLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameService;
    }

    @Override
    public void processLogsSameTime(List<LogLine> logsGroupedByTime, GameService gameService) {

        if(logsGroupedByTime!=null && logsGroupedByTime.size() > 1) {
            Collections.sort(logsGroupedByTime, new Comparator<LogLine>() {
                @Override
                public int compare(LogLine o1, LogLine o2) {
                    return Integer.compare(o1.getType().getPriority(), o2.getType().getPriority());
                }
            });
        }

        for(LogLine logLine : logsGroupedByTime) {
            try {
                switch (logLine.getType()) {
                    case INIT_GAME -> {
                        gameService.initGame();
                    }
                    case SHUT_DOWN_GAME -> {
                        gameService.shutDownGame();
                    }

                    case CLIENT_CONNECT -> {
                        ClientConnectEvent clientConnectEvent = (ClientConnectEvent) logLine;
                        gameService.playerConnect(clientConnectEvent.getPlayerId());
                    }
                    case CLIENT_BEGIN -> {
                        ClientBeginEvent clientBeginEvent = (ClientBeginEvent) logLine;
                        gameService.playerBegin(clientBeginEvent.getPlayerId());
                    }
                    case CLIENT_DISCONNECT -> {
                        ClientDisconnectEvent clientDisconnectEvent = (ClientDisconnectEvent) logLine;
                        gameService.playerDisconnect(clientDisconnectEvent.getPlayerId());

                    }
                    case CLIENT_USERINFO_CHANGED -> {
                        ClientUserinfoChangedEvent clientUserinfoChangedEvent = (ClientUserinfoChangedEvent) logLine;


                        PlayerDTO playerDTO = gameService.getPlayerById(clientUserinfoChangedEvent.getPlayerId());
                        String oldName = playerDTO.getName();
                        //if the old name is null so add the player to the ranking
                        if(oldName==null || oldName.isEmpty()) {
                            rankingService.addPlayerIfDoenstExists(clientUserinfoChangedEvent.getName());
                        }
                        else {
                            rankingService.updateName(oldName, clientUserinfoChangedEvent.getName());
                        }

                        gameService.playerUpdate(clientUserinfoChangedEvent.getPlayerId(), clientUserinfoChangedEvent.getName());

                    }

                    case ITEM -> {
                        //do nothing
                    }
                    case EXIT -> {
                        //do nothing
                    }
                    case KILL -> {
                        KillEvent killEvent = (KillEvent) logLine;
                        gameService.playerKill(killEvent.getKiller(), killEvent.getVictim(), killEvent.getMeansOfDeath());


                        if(killEvent.getKiller() == GameConstantValues.WORLD_KILLER_ID) {
                            PlayerDTO playerDTO = gameService.getPlayerById(killEvent.getVictim());
                            rankingService.addPlayerKillScore(playerDTO.getName(), GameConstantValues.SCORE_SUICIDE);
                        }
                        else {
                            PlayerDTO playerDTO = gameService.getPlayerById(killEvent.getKiller());
                            rankingService.addPlayerKillScore(playerDTO.getName(), GameConstantValues.SCORE_NORMAL_KILL);
                        }
                    }

                }
            }
            catch (PlayerAlreadyExists | PlayerDoesntExist | PlayerIsNotInTheGame e) {
                String message = String.format("Log Validation: Line %d has a problem. %s", logLine.getLineNumber(), e.getMessage());
                System.out.println(message);
            }
        }


    }


    @Override
    public LogLine parseLine(String rawLine) throws CorruptedLogLine {
        LogLine logLine = null;

        rawLine = rawLine.trim(); //remove spaces at beginning and end

        if(!rawLine.isEmpty()) {

            String[] parts = rawLine.split(" ", 3);

            if (parts.length >= 2) {
                String time = parts[0]; // time

                if (time.contains(":")) {
                    logLine = new LogLine();
                    logLine.setRawData(rawLine);

                    int timeInSeconds = getTimeInSeconds(time);
                    logLine.setTimeInSeconds(timeInSeconds);

                    LogEventTypeEnum logEventTypeEnum = readEventType(parts[1]);  // event type

                    //It can be just a line with -------
                    if (logEventTypeEnum != null) {

                        logLine.setType(logEventTypeEnum);

                        //if it has params
                        if (parts.length == 3) {
                            logLine = EventsFactory.createEvent(logLine, parts[2]);
                        }
                    }


                } else {
                    //invalid log, time invalid
                    throw new CorruptedLogLine("Invalid log line, time invalid: " + rawLine);
                }
            } else {
                //data incomplete, only time? doesnt matter
                throw new CorruptedLogLine("Invalid log line, incomplete data: " + rawLine);
            }
        }
        return logLine;
    }

    private int getTimeInSeconds(String time) throws CorruptedLogLine {
        int timeInSeconds;
        String[] timeParts = time.split(":");
        if(timeParts.length == 2) {
            try {
                int minutes = Integer.parseInt(timeParts[0]);
                int seconds = Integer.parseInt(timeParts[1]);
                timeInSeconds = minutes * 60 + seconds;
            }
            catch (NumberFormatException e) {
                throw new CorruptedLogLine(String.format("Time invalid, no numbers %s", time));
            }
        }
        else {
            throw new CorruptedLogLine(String.format("Time invalid %s", time));
        }
        return timeInSeconds;
    }

    private LogEventTypeEnum readEventType(String eventType) {
        LogEventTypeEnum type;
        String eventName = eventType.replace(":", "");

        //linear search, it´s not the best FIXME
        type = allowedEventTypes.get(eventName);

        return type;
    }



}
