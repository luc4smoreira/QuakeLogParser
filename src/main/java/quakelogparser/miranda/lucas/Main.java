package quakelogparser.miranda.lucas;

import quakelogparser.miranda.lucas.parser.QuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParserImp;
import quakelogparser.miranda.lucas.service.GameService;

public class Main {

    public static void main(String[] args) {

        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test1.log");
        gameService.debug();

    }
}
