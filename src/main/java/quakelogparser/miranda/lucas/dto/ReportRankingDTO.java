package quakelogparser.miranda.lucas.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportRankingDTO {

    private Map<Integer, PlayerDTO> ranking;

    public ReportRankingDTO() {
        ranking = new LinkedHashMap<>();
    }

    public Map<Integer, PlayerDTO> getRanking() {
        return ranking;
    }

    public void setRanking(Map<Integer, PlayerDTO> ranking) {
        this.ranking = ranking;
    }
}
