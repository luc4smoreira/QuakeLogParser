package quakelogparser.miranda.lucas.service;

import org.junit.jupiter.api.Test;
import quakelogparser.miranda.lucas.dto.GameDTO;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

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
            GameDTO gameDTO = gameService.getCurrentGameData();
            PlayerDTO playerDTO = gameDTO.getPlayerById(1);
            assertEquals(-1, playerDTO.getKills());

        } catch (PlayerAlreadyExists | PlayerDoesntExist | PlayerIsNotInTheGame e) {
            throw new AssertionError("Should not generate an error", e);
        }
    }

}