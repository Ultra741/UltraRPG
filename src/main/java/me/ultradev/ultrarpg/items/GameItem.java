package me.ultradev.ultrarpg.items;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemManager;
import me.ultradev.ultrarpg.api.util.Icon;
import org.bukkit.Material;

public enum GameItem {

    COOL_POTATO(null, "&6Cool Potato", new Icon(Material.BAKED_POTATO),
            "Yo, that's cool!", Type.MISCELLANEOUS, SubType.NONE, Rarity.SPECIAL)

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

    public enum Rarity {
        COMMON("&fCommon"),
        UNCOMMON("&aUncommon"),
        RARE("&9Rare"),
        EPIC("&5Epic"),
        LEGENDARY("&6Legendary"),
        MYTHIC("&dMythic"),
        DIVINE("&bDivine"),
        SPECIAL("&c&lSpecial")

        ;

        private final String name;
        Rarity(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public enum Type {
        MISCELLANEOUS,
        WEAPON,
        ARMOR
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
