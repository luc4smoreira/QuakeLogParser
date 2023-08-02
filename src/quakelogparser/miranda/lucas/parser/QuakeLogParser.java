package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.Main;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuakeLogParser implements IQuakeLogParser {

    private String logFile;

    public QuakeLogParser(final String fileName) {
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


                        if(line > 10) {
                            System.out.println(" Vai até a linha 10");
                            break;
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
                //invalid log
                throw new CorruptedLogLine("Invalid log line: "+rawLine);
            }
        }
        return logLine;
    }

    private LogEventTypeEnum readEventType(String eventType) {
        LogEventTypeEnum type = null;
        eventType = eventType.replace(":", "");

        for(LogEventTypeEnum logEventTypeEnum : LogEventTypeEnum.values()) {
            System.out.println(" eventType:"+eventType+ " comparando com:"+ logEventTypeEnum.getValue());
            if(eventType.equalsIgnoreCase(logEventTypeEnum.getValue())) {
                System.out.println(" ==> Achou tipo:"+ logEventTypeEnum.getValue());
                break;
            }
        }
        return type;
    }
}
