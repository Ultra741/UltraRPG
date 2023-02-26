package me.ultradev.ultrarpg.game.items;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemManager;
import me.ultradev.ultrarpg.api.util.Icon;
import me.ultradev.ultrarpg.game.stats.Stat;

public enum GameItem {

    @ItemStat(stat = Stat.HEALTH, value = 3)
    @ItemStat(stat = Stat.DEFENSE, value = 1)
    IRON_TALISMAN(null, "Iron Talisman", new Icon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVmZGRlYzM2NjU1ZDNiMzcxYzc5ZDYxMTMzNTQ4Nzc1NzcwODljMWZjYjFiM2Q4ZTAwYWYzMjYxMmYyNmYyOCJ9fX0="),
            null, Type.ACCESSORY, SubType.NONE, Rarity.UNCOMMON),

    ;

    private final ItemManager manager;

    private final String name;
    private final Icon icon;
    private final String description;

    private final Type type;
    private final SubType subType;
    private final Rarity rarity;

    GameItem(ItemManager manager, String name, Icon icon, String description, Type type, SubType subType, Rarity rarity) {
        this.manager = manager;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.type = type;
        this.subType = subType;
        this.rarity = rarity;

        if (manager != null) Main.getInstance().registerListener(manager);
    }

    public ItemManager getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public SubType getSubType() {
        return subType;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public enum Type {
        MISCELLANEOUS,
        WEAPON,
        ARMOR,
        ACCESSORY
    }

    public enum SubType {
        NONE,

        // WEAPONS
        SWORD,
        BOW,

        // ARMOR
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

}
