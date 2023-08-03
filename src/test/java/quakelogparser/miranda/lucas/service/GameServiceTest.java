package quakelogparser.miranda.lucas.service;

import org.junit.jupiter.api.Test;
import quakelogparser.miranda.lucas.constants.GameConstantValues;
import quakelogparser.miranda.lucas.constants.MeansOfDeathEnum;
import quakelogparser.miranda.lucas.dto.*;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceTest {

    private GameService gameService;

    private String name1 = "Lucas";
    private String name2 = "Moreira";
    private String name3 = "Carneiro de Miranda";

    @Test
    public void testKillByWorld() {
        try {

            gameService = new GameServiceImp();
            gameService.initGame();

            connectUser(1, name1);
            gameService.playerKill(GameConstantValues.WORLD_KILLER_ID, 1, MeansOfDeathEnum.MOD_TRIGGER_HURT.getId());

            connectUser(2, name2);
            GameDTO gameDTO = gameService.getCurrentGameData();

            PlayerConnectionDTO player1DTO = gameDTO.getPlayerById(1);
            assertEquals(-1, player1DTO.getKills());

            PlayerConnectionDTO player2DTO = gameDTO.getPlayerById(2);
            assertEquals(0, player2DTO.getKills());

            Map<String, ReportGameDTO> reports = gameService.generateMatchesReport();
            assertEquals(1, reports.size());
            for(ReportGameDTO reportGameDTO : reports.values()) {
                assertEquals(1, reportGameDTO.getTotal_kills());
                assertEquals(2, reportGameDTO.getPlayers().size());
            }


        } catch (PlayerAlreadyExists | PlayerDoesntExist | PlayerIsNotInTheGame e) {
            throw new AssertionError("Should not generate an error", e);
        }
    }

    @Test
    public void testRanking() {
        try {
            gameService = new GameServiceImp();
            gameService.initGame();



            connectUser(1, name1);

            connectUser(2, name2);

            connectUser(3, name3);

            gameService.playerKill(2, 1, MeansOfDeathEnum.MOD_ROCKET.getId());
            gameService.playerKill(2, 3, MeansOfDeathEnum.MOD_ROCKET.getId());
            gameService.playerKill(2, 3, MeansOfDeathEnum.MOD_ROCKET.getId());


            gameService.playerKill(1, 3, MeansOfDeathEnum.MOD_ROCKET.getId());


            gameService.playerKill(GameConstantValues.WORLD_KILLER_ID, 3, MeansOfDeathEnum.MOD_TRIGGER_HURT.getId());


            gameService.shutDownGame();

            ReportRankingDTO reportRankingDTO = gameService.generateRanking();
            Map<Integer, PlayerDTO> players = reportRankingDTO.getRanking();
            assertEquals(3, players.size());

            assertEquals(name2, players.get(1).getName());
            assertEquals(name1, players.get(2).getName());
            assertEquals(name3, players.get(3).getName());





        } catch (PlayerAlreadyExists | PlayerDoesntExist | PlayerIsNotInTheGame e) {
            throw new AssertionError("Should not generate an error", e);
        }

    }


    private void connectUser(int id, String name) throws PlayerAlreadyExists, PlayerDoesntExist {
        gameService.playerConnect(id);
        gameService.playerUpdate(id, name);
        gameService.playerBegin(id);

    }


}