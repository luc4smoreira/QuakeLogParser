package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.parser.LogLine;

public class KillLogEvent extends LogLine {
    private PlayerDTO killer;
    private PlayerDTO victm;
    private int method;
}
