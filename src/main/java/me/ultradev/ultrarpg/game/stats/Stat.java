package me.ultradev.ultrarpg.game.stats;

import me.ultradev.ultrarpg.game.player.ServerPlayer;

public enum Stat {

    HEALTH("Health", 'c', '\u2764', 20, -1),
    DEFENSE("Defense", 'b', '\u26E8', 0, -1),

    ;

    private final String name;
    private final char color;
    private final char symbol;

    private final int base;
    private final int max;

    Stat(String name, char color, char symbol, int base, int max) {
        this.name = name;
        this.color = color;
        this.symbol = symbol;
        this.base = base;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public char getColor() {
        return color;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getBase() {
        return base;
    }

    public int getMax() {
        return max;
    }

    public static void update(ServerPlayer player, Stat... stats) {
        for (Stat stat : stats) {
            double value = stat.getBase();
            player.getStats().put(stat, Math.min(stat.getMax(), value));
        }
    }

    public static void update(ServerPlayer player) {
        update(player, Stat.values());
    }

}
