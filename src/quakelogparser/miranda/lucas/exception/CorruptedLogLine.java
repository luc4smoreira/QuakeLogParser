package quakelogparser.miranda.lucas.exception;

/***
 * Exception used to throw error when the log is corrupted
 */
public class CorruptedLogLine extends Exception {
    public CorruptedLogLine(String error) {
        super(error);
    }
}
