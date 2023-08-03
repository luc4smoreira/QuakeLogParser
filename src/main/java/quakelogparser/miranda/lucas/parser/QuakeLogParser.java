package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;

import java.util.List;
import java.util.Map;

public interface QuakeLogParser {
    GameService parseFile(final String fileName);

    void processLogsSameTime(List<LogLine> logsGroupedByTime, GameService gameService);

    LogLine parseLine(String rawLine) throws CorruptedLogLine;
}
