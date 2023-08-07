package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.dto.ConfigDTO;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;

import java.io.FileNotFoundException;
import java.util.List;

public interface QuakeLogParser {
    GameService parseFile(ConfigDTO configDTO) throws FileNotFoundException;

    void processLogsSameTime(List<LogLine> logsGroupedByTime, GameService gameService);

    LogLine parseLine(String rawLine) throws CorruptedLogLine;
}
