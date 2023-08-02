package quakelogparser.miranda.lucas.exception;

public class NoGameInitialized extends RuntimeException {
    public NoGameInitialized() {
        super("There is no match initialized");
    }
}
