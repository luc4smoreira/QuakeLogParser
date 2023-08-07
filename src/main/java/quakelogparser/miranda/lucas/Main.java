package quakelogparser.miranda.lucas;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quakelogparser.miranda.lucas.dto.ConfigDTO;
import quakelogparser.miranda.lucas.dto.ReportGameDTO;
import quakelogparser.miranda.lucas.dto.ReportKillsByMeansDTO;
import quakelogparser.miranda.lucas.dto.ReportRankingDTO;
import quakelogparser.miranda.lucas.parser.QuakeLogParser;
import quakelogparser.miranda.lucas.parser.QuakeLogParserImp;
import quakelogparser.miranda.lucas.service.GameService;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        ConfigDTO configDTO = getConfigFromArgs(args);

        if(configDTO.isHelp()) {
            printHelpMessage();
        }
        else {

            QuakeLogParser quakeLogParser = new QuakeLogParserImp();
            try {
                GameService gameService = quakeLogParser.parseFile(configDTO);

                Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
                Map<String, ReportKillsByMeansDTO> reportDeathCause = gameService.generateKillByMeansReport();
                ReportRankingDTO ranking = gameService.generateRanking();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                System.out.println(gson.toJson(reportGame));
                System.out.println(gson.toJson(ranking));
                System.out.println(gson.toJson(reportDeathCause));


            } catch (FileNotFoundException e) {
                logger.warn(e);
            }
        }
    }

    private static ConfigDTO getConfigFromArgs(String[] args) {
        ConfigDTO configDTO = new ConfigDTO();


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case ConfigDTO.PARAM_FILE:
                    if (i + 1 < args.length) {
                        configDTO.setPathToFile(args[i + 1]);
                    }
                    break;
                case ConfigDTO.PARAM_WARNINGS:
                    configDTO.setShowValidationWarnings(true);
                    break;
                case ConfigDTO.PARAM_HELP:
                    configDTO.setHelp(true);
            }
        }

        return configDTO;
    }

    private static void printHelpMessage() {
        System.out.println("Usage:");
        System.out.println(String.format("  %s <path> : Specifies the path of the file to be processed. If blank, processes the default file: %s", ConfigDTO.PARAM_FILE, ConfigDTO.DEFAULT_LOG ));
        System.out.println(String.format("  %s : Displays validation warnings in the log.", ConfigDTO.PARAM_WARNINGS));
        System.out.println(String.format("  %s : Displays this help message.", ConfigDTO.PARAM_HELP));
    }

}
