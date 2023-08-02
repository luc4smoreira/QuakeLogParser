package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

public class GameServiceImp implements GameService{

    private int totalGames;
    @Override
    public void initGame() {
        totalGames++;
    }

    @Override
    public void shutDownGame() {

    }

    @Override
    public void playerConnect(int id) throws PlayerAlreadyExists {

    }

    @Override
    public void playerUpdate(int id, String name) throws PlayerDoesntExist {

    }

    @Override
    public void playerDisconnect(int id) throws PlayerDoesntExist {

    }

    @Override
    public void playerBegin(int id) throws PlayerDoesntExist {

    }

    @Override
    public void playerKill(int killer, int idVictim, int meansOfDeath) throws PlayerDoesntExist, PlayerIsNotInTheGame {

    }

    @Override
    public void debug() {
        System.out.println(String.format("total games: %d", totalGames));
    }
}
