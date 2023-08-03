package quakelogparser.miranda.lucas;

import com.google.gson.GsonBuilder;
import quakelogparser.miranda.lucas.dto.ReportGameDTO;
import quakelogparser.miranda.lucas.dto.ReportKillsByMeansDTO;
import quakelogparser.miranda.lucas.dto.ReportRankingDTO;
import quakelogparser.miranda.lucas.parser.QuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParserImp;
import quakelogparser.miranda.lucas.service.GameService;
import com.google.gson.Gson;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("qgames.log");

        Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
        Map<String, ReportKillsByMeansDTO> reportMeans = gameService.generateKillByMeansReport();
        ReportRankingDTO ranking = gameService.generateRanking();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(reportGame));
        System.out.println(gson.toJson(reportMeans));
        System.out.println(gson.toJson(ranking));


    }
}
