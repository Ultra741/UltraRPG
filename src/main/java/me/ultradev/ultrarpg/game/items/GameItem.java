package me.ultradev.ultrarpg.game.items;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemManager;
import me.ultradev.ultrarpg.api.items.annotations.BowAttributes;
import me.ultradev.ultrarpg.api.items.annotations.ItemStat;
import me.ultradev.ultrarpg.api.items.annotations.WeaponAttributes;
import me.ultradev.ultrarpg.api.util.Icon;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.Material;

public enum GameItem {

    @ItemStat(stat = Stat.HEALTH, value = 3)
    @ItemStat(stat = Stat.DEFENSE, value = 1)
    IRON_TALISMAN(null, "Iron Talisman", new Icon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTVmZGRlYzM2NjU1ZDNiMzcxYzc5ZDYxMTMzNTQ4Nzc1NzcwODljMWZjYjFiM2Q4ZTAwYWYzMjYxMmYyNmYyOCJ9fX0="),
            null, Type.ACCESSORY, SubType.NONE, Rarity.UNCOMMON),

    @ItemStat(stat = Stat.HEALTH, value = 20)
    @ItemStat(stat = Stat.DEFENSE, value = 100)
    STEEL_CHESTPLATE(null, "Steel Chestplate", new Icon(Material.CHAINMAIL_CHESTPLATE),
            null, Type.ARMOR, SubType.CHESTPLATE, Rarity.MYTHIC),

    @WeaponAttributes(damage = 20)
    THUNDER_AXE(null, "Thunder Axe", new Icon(Material.GOLDEN_AXE),
            null, Type.WEAPON, SubType.AXE, Rarity.LEGENDARY),

    @ItemStat(stat = Stat.POWER, value = 100)
    @ItemStat(stat = Stat.DEFENSE, value = 20)
    BERSERKER_HELMET(null, "Berserker Helmet", new Icon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWRiMWI0NTU5YmRkNDEyNzA0OTAzNzRkZmEzYzYwMmFhNDRhYjRkNTczNTgyMGYyYjQ1M2U0MjkyN2FiNjRlMSJ9fX0="),
            null, Type.ARMOR, SubType.HELMET, Rarity.EPIC),

    @WeaponAttributes(damage = 10, attackRange = 50)
    VERY_LONG_SWORD(null, "Very Long Sword", new Icon(Material.IRON_SWORD),
            "Very popular in the Hypixel Pit.", Type.WEAPON, SubType.SWORD, Rarity.MYTHIC),

    @ItemStat(stat = Stat.SPEED, value = 200)
    SNEAKERS(null, "Sneakers", new Icon(Material.NETHERITE_BOOTS),
            "Zoooooooom!", Type.ARMOR, SubType.BOOTS, Rarity.RARE),

    FUNNY_BONE(null, "Funny Bone", new Icon(Material.BONE, true),
            null, Type.MISCELLANEOUS, SubType.NONE, Rarity.COMMON),

    @WeaponAttributes(damage = 25, attackRange = 5)
    @ItemStat(stat = Stat.DEFENSE, value = 50)
    @ItemStat(stat = Stat.SPEED, value = 100)
    @ItemStat(stat = Stat.LUCK, value = 500)
    EXCALIBUR(null, "Excalibur", new Icon(Material.IRON_SWORD),
            null, Type.WEAPON, SubType.SWORD, Rarity.MYTHIC),

    @BowAttributes(damage = 999, arrowsShot = 3)
    COOL_BOW(null, "Cool Bow", new Icon(Material.BOW),
            "Cool.", Type.WEAPON, SubType.BOW, Rarity.RARE),

    @BowAttributes(damage = 1000000, arrowsShot = 20)
    HYPER_BOW(null, "Hyperbow", new Icon(Material.BOW),
            null, Type.WEAPON, SubType.BOW, Rarity.MYTHIC),

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
        AXE,
        BOW,

        // ARMOR
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

}
