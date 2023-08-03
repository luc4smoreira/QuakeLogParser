package quakelogparser.miranda.lucas.dto;

import java.util.Map;

public class ReportKillsByMeansDTO {
    private Map<String, Integer> kills_by_means;

    public Map<String, Integer> getKills_by_means() {
        return kills_by_means;
    }

    public void setKills_by_means(Map<String, Integer> kills_by_means) {
        this.kills_by_means = kills_by_means;
    }
}
