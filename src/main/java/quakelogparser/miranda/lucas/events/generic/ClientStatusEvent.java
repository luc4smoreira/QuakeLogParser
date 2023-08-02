package quakelogparser.miranda.lucas.events.generic;

import quakelogparser.miranda.lucas.parser.LogLine;

public abstract class ClientStatusEvent extends LogLine {
    private int playerId;
    public ClientStatusEvent(int id) {
        this.playerId = id;
    }

    public int getPlayerId() {
        return playerId;
    }
}
