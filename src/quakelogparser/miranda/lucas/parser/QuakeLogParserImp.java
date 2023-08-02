package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.Main;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;
import quakelogparser.miranda.lucas.service.GameServiceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuakeLogParserImp implements QuakeLogParser {

    private String logFile;

    private GameService gameService;

    public QuakeLogParserImp(final String fileName) {
        this.logFile = fileName;

        //open the file and prepare to read it
        InputStream is = Main.class.getClassLoader().getResourceAsStream(logFile);
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
    }

    private LogLine parseLine(String rawLine) throws CorruptedLogLine {
        LogLine logLine = null;

        System.out.println(rawLine);
        String[] parts = rawLine.split(" ");

        if (parts.length >= 2) {
            String time = parts[0]; // time

            if(time.contains(":")) {
                logLine = new LogLine();
                logLine.setRawData(rawLine);

                logLine.setTime(time);
                String eventType = parts[1]; // event type


                LogEventTypeEnum logEventTypeEnum = readEventType(eventType);
                logLine.setType(logEventTypeEnum);

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
        LogEventTypeEnum type = null;
        eventType = eventType.replace(":", "");

        //linear search, it´s not the best FIXME
        for(LogEventTypeEnum logEventTypeEnum : LogEventTypeEnum.values()) {
            if(eventType.equalsIgnoreCase(logEventTypeEnum.getValue())) {
                break;
            }
        }
        return type;
    }
}
