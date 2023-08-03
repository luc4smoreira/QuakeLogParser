package quakelogparser.miranda.lucas.dto;

public class PlayerDTO {

    private String name;
    private int kills;


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
