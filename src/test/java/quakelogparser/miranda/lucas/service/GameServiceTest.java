package quakelogparser.miranda.lucas.service;

import org.junit.jupiter.api.Test;
import quakelogparser.miranda.lucas.dto.GameDTO;
import quakelogparser.miranda.lucas.dto.PlayerConnectionDTO;
import quakelogparser.miranda.lucas.dto.ReportGameDTO;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    public void testKillByWorld() {
        try {

            GameService gameService = new GameServiceImp();
            gameService.initGame();
            gameService.playerConnect(1);
            gameService.playerBegin(1);
            gameService.playerKill(GameServiceImp.WORLD_KILLER_ID, 1, 22);

            gameService.playerConnect(2);
            gameService.playerBegin(2);
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



}