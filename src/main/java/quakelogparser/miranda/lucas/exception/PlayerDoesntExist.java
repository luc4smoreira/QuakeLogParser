package quakelogparser.miranda.lucas.exception;

public class PlayerDoesntExist extends Exception {

    public PlayerDoesntExist(int playerId) {
        super(String.format("This player id doesn´t exist in the game: %d", playerId));
    }
}
