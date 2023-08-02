package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.events.generic.ClientStatusEvent;

public class ClientDisconnectEvent extends ClientStatusEvent {

    public ClientDisconnectEvent(int id) {
        super(id);
    }

}
