package quakelogparser.miranda.lucas.events;

import quakelogparser.miranda.lucas.parser.LogLine;

public class KillEvent extends LogLine {
    private int killer;
    private int victim;
    private int meansOfDeath;

    public KillEvent(int killer, int victim, int meansOfDeath) {
        this.killer = killer;
        this.victim = victim;
        this.meansOfDeath = meansOfDeath;
    }

    public int getKiller() {
        return killer;
    }

    public int getVictim() {
        return victim;
    }

    public int getMeansOfDeath() {
        return meansOfDeath;
    }
}
