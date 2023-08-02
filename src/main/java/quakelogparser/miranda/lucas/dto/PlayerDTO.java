package quakelogparser.miranda.lucas.dto;

public class PlayerDTO {
    private int id;
    private String name;
    private int kills;
    private boolean connected;
    private boolean begin;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addKills(int value) {
        this.kills+=value;
    }

    public int getKills() {
        return kills;
    }
}
