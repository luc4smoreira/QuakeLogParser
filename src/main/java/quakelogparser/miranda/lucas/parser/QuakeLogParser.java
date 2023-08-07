package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.dto.ConfigDTO;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;

import java.io.FileNotFoundException;
import java.util.List;

public interface QuakeLogParser {
    GameService parseFile(ConfigDTO configDTO) throws FileNotFoundException;

    /**
     * It will sort the lines from the match with the same time using Event Priority.
     * It is necessary to group logs by time, and process them by ordering by event type,
     * because it is possible to have this scenario:
     * 0:04 Kill: 5 3 18: Chessus killed Dono da Bola by MOD_TELEFRAG
     * 0:04 ClientBegin: 5 #it shouldn't be possible to kill someone not in the game
     *
     * @param logsGroupedByTime List of logs from the same match with the same time
     * @param gameService Service to process the data.
     */
    void processLogsFromSameMatchAndTime(List<LogLine> logsGroupedByTime, GameService gameService);

    LogLine parseLine(String rawLine) throws CorruptedLogLine;
}
