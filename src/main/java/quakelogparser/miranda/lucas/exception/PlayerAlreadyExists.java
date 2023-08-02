package quakelogparser.miranda.lucas.exception;

public class PlayerAlreadyExists extends Exception{
    public PlayerAlreadyExists(int playerId) {
        super(String.format("This player id is already connected to the game: %d", playerId));
    }
}
