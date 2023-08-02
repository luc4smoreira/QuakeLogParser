package quakelogparser.miranda.lucas.parser;

import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;

public interface QuakeLogParser {
    GameService parseFile(final String fileName);

    LogLine parseLine(String rawLine) throws CorruptedLogLine;
}
