package quakelogparser.miranda.lucas.dto;

public class PlayerConnectionDTO extends PlayerDTO {

    private boolean connected;
    private boolean begin;



    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }


}
