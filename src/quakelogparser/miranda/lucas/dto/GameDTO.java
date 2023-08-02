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



    public PlayerDTO getPlayerById(int id) {
        return players.get(id);
    }

    public void addNewPlayer(int id) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(id);
        players.put(id, playerDTO);
    }

    public void printPlayers() {
        for(PlayerDTO playerDTO : players.values()) {
            System.out.println(String.format("id: %d name: %s", playerDTO.getId(), playerDTO.getName()));
        }
    }
}
