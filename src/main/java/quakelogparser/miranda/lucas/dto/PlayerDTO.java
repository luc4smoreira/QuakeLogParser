package quakelogparser.miranda.lucas.dto;

public class PlayerDTO {

    private int id;
    private String name;
    private int kills;

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

    public void addKills(int value) {
        this.kills+=value;
    }

    public int getKills() {
        return kills;
    }
}
