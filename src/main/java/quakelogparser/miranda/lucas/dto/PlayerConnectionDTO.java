package quakelogparser.miranda.lucas.dto;

public class PlayerConnectionDTO extends PlayerDTO {

    private int id;
    private boolean connected;
    private boolean begin;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
