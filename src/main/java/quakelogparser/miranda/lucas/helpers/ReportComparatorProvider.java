package quakelogparser.miranda.lucas.helpers;

import quakelogparser.miranda.lucas.dto.PlayerDTO;

import java.util.Comparator;

public class ReportComparatorProvider {

    public static Comparator<PlayerDTO> getComparatorPlayerByKills() {
        return new Comparator<PlayerDTO>() {
            @Override
            public int compare(PlayerDTO o1, PlayerDTO o2) {
                return Integer.compare(o2.getKills(), o1.getKills());
            }
        };
    }


}
