package quakelogparser.miranda.lucas.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO with data necessary to generate the game report
 */
public class GameReportDTO {
    private int totalKills;
    private List<String> players;
    private Map<String, Integer> kills;

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
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
