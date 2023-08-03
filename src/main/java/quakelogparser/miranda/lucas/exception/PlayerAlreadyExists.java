package quakelogparser.miranda.lucas.exception;

public class PlayerAlreadyExists extends Exception{
    public PlayerAlreadyExists(int playerId) {
        super(String.format("This player id is already connected to the game: %d", playerId));
    }

    public PlayerAlreadyExists(String name) {
        super(String.format("This player name already exists: %s", name));
    }
}
