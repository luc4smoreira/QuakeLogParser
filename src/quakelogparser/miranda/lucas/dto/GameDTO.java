package quakelogparser.miranda.lucas.dto;

import java.util.HashMap;
import java.util.Map;

public class GameDTO {
    private int id;
    private boolean shutdown;
    private boolean error;
    private Map<Integer, PlayerDTO> players;

    public GameDTO() {
        players = new HashMap<>();
    }

    public Map<Integer, PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, PlayerDTO> players) {
        this.players = players;
    }
}
