package quakelogparser.miranda.lucas.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quakelogparser.miranda.lucas.Main;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.dto.ConfigDTO;
import quakelogparser.miranda.lucas.events.*;
import quakelogparser.miranda.lucas.exception.*;
import quakelogparser.miranda.lucas.helpers.ReportComparatorProvider;
import quakelogparser.miranda.lucas.service.GameService;
import quakelogparser.miranda.lucas.service.GameServiceImp;

import java.io.*;
import java.util.*;

public class QuakeLogParserImp implements QuakeLogParser {

    private static final Logger logger = LogManager.getLogger(QuakeLogParserImp.class);

    private Map<String, LogEventTypeEnum> allowedEventTypes;

    private boolean warnings;



    public QuakeLogParserImp() {

        allowedEventTypes = new HashMap<>();

        for(LogEventTypeEnum eventType : LogEventTypeEnum.values()) {
            allowedEventTypes.put(eventType.getValue(), eventType);
        }


    }

    @Override
    public GameService parseFile(ConfigDTO configDTO) throws FileNotFoundException {

        GameService gameService = new GameServiceImp();


        this.warnings = configDTO.isShowValidationWarnings();


        InputStreamReader inputStreamReader;

        File file = new File(configDTO.getPathToFile());

        //if the file doesn't exit in file system
        if (!file.exists() || !file.isFile()) {
            //try to open the file inside the jar
            InputStream is = Main.class.getClassLoader().getResourceAsStream(configDTO.getPathToFile());
            inputStreamReader = new InputStreamReader(is);
        }
        else {
            //open the file and prepare to read it
            FileInputStream fis = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fis);

        }

        //if didn't find the file inside jar and in file sytem, throw error
        if(inputStreamReader==null) {
            String mesage = String.format(" *** The file was not found: %s", configDTO.getPathToFile());
            System.out.println(mesage);
            throw new RuntimeException(mesage);
        }

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
                                processLogsFromSameMatchAndTime(batchLogsByTime, gameService);
                                batchLogsByTime.clear(); //clear the list
                                batchLogsByTime.add(logLine); //add the log to the next batch
                                timeLogsBatch = logLine.getTimeInSeconds(); //update the time associeated with the next batch
                            }
                        }
                        else {
                            //if there is a line empty or no type like (----) so process the data
                            processLogsFromSameMatchAndTime(batchLogsByTime, gameService);
                            batchLogsByTime.clear(); //clear the list
                        }


                    }
                }
                catch (CorruptedLogLine e) {
                    if(warnings) {
                        String message = String.format("Validation Warning: Line %d looks corrupted. %s", lineNumber, e.getMessage());
                        logger.warn(message);
                    }
                }
                catch (NoGameInitialized e) {
                    String message = String.format("Error: Line %d %s", lineNumber, e.getMessage());
                    logger.error(message);
                    throw e;
                }

                rawLine = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameService;
    }

    @Override
    public void processLogsFromSameMatchAndTime(List<LogLine> logsGroupedByTime, GameService gameService) {


        if(logsGroupedByTime!=null && logsGroupedByTime.size() > 1) {
            Collections.sort(logsGroupedByTime, ReportComparatorProvider.getComparadorLogLinesByType());
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
                    }

                }
            }
            catch (PlayerAlreadyExists | PlayerDoesntExist | PlayerIsNotInTheGame e) {
                if(warnings) {
                    String message = String.format("Log Validation: Line %d has a problem. %s", logLine.getLineNumber(), e.getMessage());
                    logger.warn(message);
                }
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


        type = allowedEventTypes.get(eventName);

        return type;
    }


    //TODO adicionar logs
    //TODO fazer o test6 e passar neste teste, depende então de um ponto que é o id mudar ao reconectar
    //FIXME considerar os jogadores na partida somente quando ocorrer o begin, anterior a isso ou desconect, tirar o jogador da partida e ir atualizando os dados dele, quando ocorrer o begin, buscar o jogador na partida que estava desconectado e ligar a ele novamente

}
