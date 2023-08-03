package quakelogparser.miranda.lucas.dto;

import java.util.List;

public class ReportRankingDTO {

    private List<PlayerDTO> ranking;

    public List<PlayerDTO> getRanking() {
        return ranking;
    }

    public void setRanking(List<PlayerDTO> ranking) {
        this.ranking = ranking;
    }
}
