package me.ultradev.ultrarpg.game.items;

public enum Rarity {

    COMMON('f'),
    UNCOMMON('a'),
    RARE('9'),
    EPIC('5'),
    LEGENDARY('6'),
    MYTHIC('d'),
    DIVINE('b'),
    SPECIAL('c')

    ;

    private final char color;

    Rarity(char color) {
        this.color = color;
    }

    public char getColor() {
        return color;
    }

    public String getColorCode() {
        return "&" + color;
    }

    public String getName() {
        return getColorCode() + "&l" + this;
    }

}
