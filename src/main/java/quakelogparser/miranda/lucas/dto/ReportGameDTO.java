package quakelogparser.miranda.lucas.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO with data necessary to generate the game report
 */
public class ReportGameDTO {
    private int total_kills;
    private List<String> players;
    private Map<String, Integer> kills;

    public int getTotal_kills() {
        return total_kills;
    }

    public void setTotal_kills(int total_kills) {
        this.total_kills = total_kills;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public Map<String, Integer> getKills() {
        return kills;
    }

    public void setKills(Map<String, Integer> kills) {
        this.kills = kills;
    }

}
