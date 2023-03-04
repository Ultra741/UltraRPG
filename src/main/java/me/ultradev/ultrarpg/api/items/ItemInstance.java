package me.ultradev.ultrarpg.api.items;

import me.ultradev.ultrarpg.api.items.annotations.ItemStat;
import me.ultradev.ultrarpg.api.items.annotations.ItemStats;
import me.ultradev.ultrarpg.api.items.annotations.WeaponStats;
import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.api.util.NumberUtil;
import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ItemInstance {

    private final GameItem item;
    private final Map<Stat, Double> stats = new HashMap<>();

    public ItemInstance(GameItem item) {
        this.item = item;
    }

    public ItemInstance(ItemStack item) {
        String id = NBTEditor.getString(item, "item_id");
        if (id.equals("")) throw new IllegalArgumentException("Couldn't create an instance for this item!");
        this.item = GameItem.valueOf(id);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        for (NamespacedKey key : meta.getPersistentDataContainer().getKeys()) {
            if (key.getKey().startsWith("stat.")) {
                stats.put(Stat.valueOf(key.getKey().replaceFirst("stat.", "").toUpperCase()),
                        NBTEditor.getDouble(item, key.getKey()));
            }
        }
    }

    public GameItem getItem() {
        return item;
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public ItemBuilder format() {
        ItemBuilder builder = item.getIcon().toItemBuilder()
                .setName(item.getRarity().getColorCode() + item.getName())
                .addTag("item_id", item.toString())
                .hideFlags()
                .setUnbreakable(true);

        try {
            boolean hasStats = false;
            Field field = item.getClass().getField(item.toString());
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ItemStats itemStats) {
                    hasStats = true;
                    for (ItemStat itemStat : itemStats.value()) {
                        builder.addTag("stat." + itemStat.stat(), itemStat.value())
                                .addLore("&7" + itemStat.stat().getName() + ": &" +
                                        itemStat.stat().getColor() + "+" + NumberUtil.toFancyNumber(itemStat.value()) +
                                        itemStat.stat().getSymbol());
                    }
                } else if (annotation instanceof ItemStat itemStat) {
                    hasStats = true;
                    builder.addTag("stat." + itemStat.stat(), itemStat.value())
                            .addLore("&7" + itemStat.stat().getName() + ": &" +
                                    itemStat.stat().getColor() + "+" + NumberUtil.toFancyNumber(itemStat.value()) +
                                    itemStat.stat().getSymbol());
                } else if (item.getType().equals(GameItem.Type.WEAPON) && annotation instanceof WeaponStats weaponStats) {
                    hasStats = true;
                    builder.addTag("weapon_damage", weaponStats.damage())
                            .addLore("&7Damage: &c" + weaponStats.damage() + "\uD83D\uDDE1");
                    if (weaponStats.attackRange() != -1) {
                        builder.addTag("attack_range", weaponStats.attackRange())
                                .addLore("&7Attack Range: &b" + weaponStats.attackRange() + "\uD83D\uDD31");
                    }
                }
            }
            if (hasStats) {
                builder.addLore("");
            }
        } catch (NoSuchFieldException ignored) {
        }

        if (item.getDescription() != null) {
            builder.addLore("&7" + item.getDescription(), "");
        }

        if (!item.getType().equals(GameItem.Type.MISCELLANEOUS)) {
            if (item.getSubType().equals(GameItem.SubType.NONE)) {
                builder.addLore(item.getRarity().getName() + " " + item.getType().toString().replaceAll("_", ""));
            } else {
                builder.addLore(item.getRarity().getName() + " " + item.getSubType().toString().replaceAll("_", ""));
            }
        } else {
            builder.addLore(item.getRarity().getName());
        }

        return builder;
    }

}
