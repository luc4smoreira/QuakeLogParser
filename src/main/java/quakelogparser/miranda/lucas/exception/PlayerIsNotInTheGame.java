package quakelogparser.miranda.lucas.exception;

public class PlayerIsNotInTheGame extends Exception{
    public PlayerIsNotInTheGame(int playerId, boolean connected, boolean begin) {
        super(String.format("This player id is not in the game: player.id: %d player.connected: %b player.begin: %b", playerId, connected, begin));
    }
}
