package quakelogparser.miranda.lucas;

import quakelogparser.miranda.lucas.parser.IQuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParser;

public class Main {

    public static void main(String[] args) {

        IQuakeLogParser quakeLogParser = new QuakeLogParser("qgames.log");

    }
}
