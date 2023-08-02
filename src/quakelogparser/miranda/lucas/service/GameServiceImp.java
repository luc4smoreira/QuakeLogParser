package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.dto.GameDTO;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.exception.NoGameInitialized;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

import java.util.ArrayList;
import java.util.List;

public class GameServiceImp implements GameService{

    private List<GameDTO> matches;
    private int totalGames;

    private GameDTO currentMatch;

    public GameServiceImp(){
        matches = new ArrayList<>();

    }

    @Override
    public void initGame() {
        if(currentMatch!=null) {
            matches.add(currentMatch);
        }
        currentMatch = new GameDTO();
        totalGames++;
    }

    @Override
    public void shutDownGame() {
        validate();

        if(currentMatch!=null) {
            matches.add(currentMatch);
        }
        else {
            throw new RuntimeException();
        }
        currentMatch = null;
    }

    @Override
    public void playerConnect(int id) throws PlayerAlreadyExists {
        validate();

        PlayerDTO playerDTO = currentMatch.getPlayers().get(id);
        if (playerDTO != null) {

            if(playerDTO.isConnected()) {
                throw new PlayerAlreadyExists(id);
            }
            else {
                playerDTO.setConnected(true);
            }
        }
        currentMatch.getPlayers().put(id, new PlayerDTO());

    }

    @Override
    public void playerUpdate(int id, String name) throws PlayerDoesntExist {
        validate();
    }

    @Override
    public void playerDisconnect(int id) throws PlayerDoesntExist {
        validate();

        PlayerDTO playerDTO = currentMatch.getPlayers().get(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerDTO.setConnected(false);
        playerDTO.setBegin(false);
    }

    @Override
    public void playerBegin(int id) throws PlayerDoesntExist {
        validate();

        PlayerDTO playerDTO = currentMatch.getPlayers().get(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerDTO.setBegin(true);
    }

    @Override
    public void playerKill(int killer, int idVictim, int meansOfDeath) throws PlayerDoesntExist, PlayerIsNotInTheGame {
        validate();
    }

    @Override
    public void debug() {
        System.out.println(String.format("total games: %d", totalGames));
    }

    private void validate() {
        if(currentMatch==null) {
            throw new NoGameInitialized();
        }
    }
}
