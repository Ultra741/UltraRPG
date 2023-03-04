package me.ultradev.ultrarpg.game.stats;

public enum Stat {

    HEALTH("Health", 'c', "\u2764", 100, -1),
    DEFENSE("Defense", 'b', "\u26E8", 0, -1),
    POWER("Power", 'c', "\uD83D\uDD25", 0, -1),
    SPEED("Speed", 'e', "\u26A1", 0, 300),

    ;

    private final String name;
    private final char color;
    private final String symbol;

    private final int base;
    private final int max;

    Stat(String name, char color, String symbol, int base, int max) {
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

    public String getSymbol() {
        return symbol;
    }

    public double getBase() {
        return base;
    }

    public int getMax() {
        return max;
    }

}
