package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.dto.GameDTO;
import quakelogparser.miranda.lucas.dto.GameReportDTO;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

import java.util.Map;

public interface GameService {

    /**
     * Initilize a new game
     */
    void initGame();

    /**
     * Shut down the current game and disconnect all players
     */
    void shutDownGame();


    /***
     * Add a new player to the game with a new Id or reconnect a disconnected player with the same id
     *
     * @param id Player id, unique
     * @throws PlayerAlreadyExists Throws if the game already have this player id *connected*
     */
    void playerConnect(int id) throws PlayerAlreadyExists;


    /**
     * Update the name associated with the player defined by the id
     * @param id Player id
     * @param name New name
     * @throws PlayerDoesntExist Throws if the player is not connected
     */
    void playerUpdate(int id, String name) throws PlayerDoesntExist;


    /**
     * Disconnect player from game
     *
     * @param id Player ud
     * @throws PlayerDoesntExist
     */
    void playerDisconnect(int id) throws PlayerDoesntExist;

    /**
     * Allow player to kill or be killed
     *
     * @param id Player id already connected
     * @throws PlayerDoesntExist Throws if the player is not connected
     */
    void playerBegin(int id) throws PlayerDoesntExist;


    /**
     * Kill a player
     *
     * @param idKiller Killer player id or 1022 for World
     * @param idVictim Victim player id
     * @param meansOfDeath How the victim died
     * @throws PlayerDoesntExist If some player id is not connected
     * @throws PlayerIsNotInTheGame If some player id didn´t call begin
     */
    void playerKill(int idKiller, int idVictim, int meansOfDeath) throws PlayerDoesntExist, PlayerIsNotInTheGame;

    GameDTO getCurrentGameData();


    Map<String, GameReportDTO> generateMatchesReport();

}
