package quakelogparser.miranda.lucas.service;

public interface GameService {

    void initGame();

    void shutDownGame();

    void addPlayer(int id);

    void updatePlayer(String name);

    void killPlayer(int idVictim, int meansOfDeath, Integer killer);
}
