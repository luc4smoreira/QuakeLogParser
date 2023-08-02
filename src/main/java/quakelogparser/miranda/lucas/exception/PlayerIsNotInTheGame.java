package quakelogparser.miranda.lucas.exception;

public class PlayerIsNotInTheGame extends Exception{
    public PlayerIsNotInTheGame(int playerId) {
        super(String.format("This player id is not in the game: %d", playerId));
    }
}
