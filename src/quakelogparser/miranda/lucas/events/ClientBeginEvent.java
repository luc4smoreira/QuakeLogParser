package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.events.generic.ClientStatusEvent;

public class ClientBeginEvent extends ClientStatusEvent {

    public ClientBeginEvent(int id) {
        super(id);
    }
}
