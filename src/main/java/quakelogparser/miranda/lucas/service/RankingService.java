package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;

public interface RankingService {

    /**
     * Add the player to the ranking, ignore if already exists.
     *
     * @param name player name
     */
    void addPlayerIfDoenstExists(String name) throws PlayerDoesntExist, PlayerAlreadyExists;


    /**
     * Update the user name in the ranking
     *
     * @param oldName cant be null
     * @param name new name
     * @throws PlayerDoesntExist
     * @throws PlayerAlreadyExists
     */
    void updateName(String oldName, String name) throws PlayerDoesntExist, PlayerAlreadyExists;

    void addPlayerKillScore(String name, int value) throws PlayerDoesntExist;

}