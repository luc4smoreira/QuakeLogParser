package quakelogparser.miranda.lucas.dto;

import quakelogparser.miranda.lucas.constants.MeansOfDeathEnum;
import quakelogparser.miranda.lucas.helpers.ReportComparatorProvider;

import java.util.*;

public class GameDTO {
    private int id;
    private boolean shutdown;
    private int errors;
    private Map<Integer, PlayerConnectionDTO> players;
    private Map<Integer, Integer> killsByMeans;
    private int totalKills;

    public GameDTO() {
        players = new HashMap<>();
        killsByMeans = new HashMap<>();
    }



    public PlayerConnectionDTO getPlayerById(int id) {
        return players.get(id);
    }

    public void addNewPlayer(int id) {
        PlayerConnectionDTO playerConnectionDTO = new PlayerConnectionDTO();
        playerConnectionDTO.setId(id);
        playerConnectionDTO.setConnected(true);
        players.put(id, playerConnectionDTO);
    }

    public List<String> getPlayersNames() {
        List<String> playersNames = new ArrayList<>();
        for(PlayerConnectionDTO playerConnectionDTO : players.values()) {
            playersNames.add(playerConnectionDTO.getName());
        }
        return playersNames;
    }

    public Map<String, Integer> getPlayersNamesWithKillsSorted() {
        //LinkedHashMap the insertion order matters
        Map<String, Integer> playersKills = new LinkedHashMap<>();

        //This list will be used to sort the players in DESC order using kills
        List<PlayerConnectionDTO> playersList = new ArrayList<>();

        for(PlayerConnectionDTO playerConnectionDTO : players.values()) {
            playersList.add(playerConnectionDTO);
        }

        //Sort the list, because it is a ranking
        Collections.sort(playersList, ReportComparatorProvider.getComparatorPlayerByKills());

        for(PlayerConnectionDTO playerConnectionDTO : playersList) {
            playersKills.put(playerConnectionDTO.getName(), playerConnectionDTO.getKills());
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

    public Map<Integer, PlayerConnectionDTO> getPlayers() {
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
