package quakelogparser.miranda.lucas.dto;

import java.util.HashMap;
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

    public void printPlayers() {
        for(PlayerDTO playerDTO : players.values()) {
            System.out.println(String.format("id: %d name: %s kills: %d", playerDTO.getId(), playerDTO.getName(), playerDTO.getKills()));
        }
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


}
