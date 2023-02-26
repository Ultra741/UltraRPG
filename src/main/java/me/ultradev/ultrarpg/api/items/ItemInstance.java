package me.ultradev.ultrarpg.api.items;

import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.api.util.NumberUtil;
import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.items.ItemStat;
import me.ultradev.ultrarpg.game.items.ItemStats;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ItemInstance {

    private final GameItem item;

    public ItemInstance(GameItem item) {
        this.item = item;
    }

    public ItemInstance(ItemStack item) {
        String id = NBTEditor.getString(item, "item_id");
        if (id.equals("")) throw new IllegalArgumentException("Couldn't create an instance for this item!");
        this.item = GameItem.valueOf(id);
    }

    public ItemBuilder format() {
        ItemBuilder builder = item.getIcon().toItemBuilder()
                .setName(item.getRarity().getColorCode() + item.getName())
                .addItemTag("item_id", item.toString())
                .hideFlags()
                .setUnbreakable(true);

        try {
            Field field = item.getClass().getField(item.toString());
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ItemStats itemStats) {
                    for (ItemStat itemStat : itemStats.value()) {
                        builder.addLore("&7" + itemStat.stat().getName() + ": &" + itemStat.stat().getColor() + "+" + NumberUtil.toFancyNumber(itemStat.value()) + itemStat.stat().getSymbol());
                    }
                    builder.addLore("");
                    break;
                }
            }
            if (field.isAnnotationPresent(ItemStat.class)) {
                builder.addLore("");
            }
        } catch (NoSuchFieldException ignored) {}

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
