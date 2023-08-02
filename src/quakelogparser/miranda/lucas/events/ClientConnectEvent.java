package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.events.generic.ClientStatusEvent;
import quakelogparser.miranda.lucas.parser.LogLine;

public class ClientConnectEvent extends ClientStatusEvent {

    public ClientConnectEvent(int id) {
        super(id);
    }

}
