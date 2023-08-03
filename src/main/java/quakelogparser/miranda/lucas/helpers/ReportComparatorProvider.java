package quakelogparser.miranda.lucas.helpers;

import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.parser.LogLine;

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

    public static Comparator<LogLine> getComparadorLogLinesByType() {
        return new Comparator<LogLine>() {
            @Override
            public int compare(LogLine o1, LogLine o2) {
                int value = Integer.compare(o1.getType().getPriority(), o2.getType().getPriority());

                //if is the same type and also the same time, so the line number is important
                if(value==0) {
                    value = Integer.compare(o1.getLineNumber(), o2.getLineNumber());
                }
                return value;
            }
        };
    }


}
