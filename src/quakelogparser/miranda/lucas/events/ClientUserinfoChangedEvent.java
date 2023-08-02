package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.events.generic.ClientStatusEvent;

public class ClientUserinfoChangedEvent extends ClientStatusEvent {
    private String name;
    public ClientUserinfoChangedEvent(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
