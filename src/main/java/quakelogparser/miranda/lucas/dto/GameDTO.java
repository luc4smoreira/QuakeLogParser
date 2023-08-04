package quakelogparser.miranda.lucas.dto;

import quakelogparser.miranda.lucas.constants.MeansOfDeathEnum;
import quakelogparser.miranda.lucas.helpers.ReportComparatorProvider;
import quakelogparser.miranda.lucas.service.PlayerHistoryServiceImp;

import java.util.*;

public class GameDTO extends PlayerHistoryServiceImp {
    private int id;
    private boolean shutdown;
    private int errors;

    //when the player is connected use id
    private Map<Integer, PlayerConnectionDTO> playersConnected;
    private Map<Integer, Integer> killsByMeans;
    private int totalKills;

    public GameDTO() {
        playersConnected = new HashMap<>();
        killsByMeans = new HashMap<>();
    }



    public PlayerConnectionDTO getPlayerById(int id) {
        return playersConnected.get(id);
    }

    public void connectPlayer(int id) {
        PlayerConnectionDTO playerConnectionDTO = new PlayerConnectionDTO();
        playerConnectionDTO.setId(id);
        playerConnectionDTO.setConnected(true);
        playersConnected.put(id, playerConnectionDTO);
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalKills() {
        return totalKills;
    }




    public List<String> getPlayersLastNames() {
        List<String> playersNames = new ArrayList<>();
        for(PlayerDTO playerDTO : players.values()) {
            playersNames.add(playerDTO.getName());
        }
        return playersNames;
    }

    public Map<String, Integer> getPlayersNamesWithKillsSorted() {
        //LinkedHashMap the insertion order matters
        Map<String, Integer> playersKills = new LinkedHashMap<>();


        List<PlayerDTO> playersList = getListOfPlayersOrderByKills();

        for(PlayerDTO playerDTO : playersList) {
            playersKills.put(playerDTO.getName(), playerDTO.getKills());
        }

        return playersKills;
    }

    public Map<String, Integer> getKillByMeansWithName() {
        Map<String, Integer> killByMeans = new HashMap<>();


        //this map is used to know the name associated with each id in killsByMeans
        Map<Integer, String> mapNameById = new HashMap<>();
        for(MeansOfDeathEnum meansOfDeathEnum : MeansOfDeathEnum.values()) {
            mapNameById.put(meansOfDeathEnum.getId(), meansOfDeathEnum.name());
        }

        for(Map.Entry<Integer, Integer> entry : killsByMeans.entrySet()) {
            String name = mapNameById.get(entry.getKey());
            killByMeans.put(name, entry.getValue());
        }

        return killByMeans;
    }

}
