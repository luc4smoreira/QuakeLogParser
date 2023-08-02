package quakelogparser.miranda.lucas.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDTO {
    private int id;
    private boolean shutdown;
    private int errors;
    private Map<Integer, PlayerDTO> players;

    private Map<Integer, Integer> killsByMeans;
    private int totalKills;

    public GameDTO() {
        players = new HashMap<>();
        killsByMeans = new HashMap<>();
    }



    public PlayerDTO getPlayerById(int id) {
        return players.get(id);
    }

    public void addNewPlayer(int id) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(id);
        playerDTO.setConnected(true);
        players.put(id, playerDTO);
    }

    public List<String> getPlayersNames() {
        List<String> playersNames = new ArrayList<>();
        for(PlayerDTO playerDTO : players.values()) {
            playersNames.add(playerDTO.getName());
        }
        return playersNames;
    }

    public Map<String, Integer> getPlayersNamesWithKills() {
        Map<String, Integer> playersKills = new HashMap<>();

        for(PlayerDTO playerDTO : players.values()) {
            playersKills.put(playerDTO.getName(), playerDTO.getKills());
        }
        return playersKills;
    }

    public void addKill(int meansOfDeath) {
        totalKills++;
        Integer totalKillsByThisMean = killsByMeans.get(meansOfDeath);

        if(totalKillsByThisMean == null) {
            //if it is null, só this is the first
            totalKillsByThisMean = 1;
        }
        else {
            totalKillsByThisMean ++;
        }
        //update the value, Integer class is imutable
        killsByMeans.put(meansOfDeath, totalKillsByThisMean);

    }

    public Map<Integer, PlayerDTO> getPlayers() {
        return players;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalKills() {
        return totalKills;
    }
}
