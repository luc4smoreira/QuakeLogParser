package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;

public interface RankingService {

    /**
     * Add the player to the ranking, ignore if they is already exists
     *
     * @param id player id
     */
    void addPlayer(int id);

    void updateName(int id, String name) throws PlayerDoesntExist;

    void addPlayerKillScore(int id, int value);

}
