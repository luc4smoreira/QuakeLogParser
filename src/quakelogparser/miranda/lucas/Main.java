package quakelogparser.miranda.lucas;

import quakelogparser.miranda.lucas.parser.QuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParserImp;

public class Main {

    public static void main(String[] args) {

        QuakeLogParser quakeLogParser = new QuakeLogParserImp("qgames.log");

    }
}
