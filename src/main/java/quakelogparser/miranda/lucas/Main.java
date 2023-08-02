package quakelogparser.miranda.lucas;

import com.google.gson.GsonBuilder;
import quakelogparser.miranda.lucas.dto.GameReportDTO;
import quakelogparser.miranda.lucas.parser.QuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParserImp;
import quakelogparser.miranda.lucas.service.GameService;
import com.google.gson.Gson;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test1.log");

        Map<String, GameReportDTO> report = gameService.generateMatchesReport();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(report));

    }
}
