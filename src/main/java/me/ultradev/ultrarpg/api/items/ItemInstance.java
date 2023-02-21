package me.ultradev.ultrarpg.api.items;

import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.api.util.StringUtil;
import me.ultradev.ultrarpg.items.GameItem;
import org.bukkit.inventory.ItemStack;

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

    public GameItem getItem() {
        return item;
    }

    public ItemBuilder format() {
        ItemBuilder builder = item.getIcon().toItemBuilder()
                .setName(item.getName())
                .addItemTag("item_id", item.toString())
                .hideFlags()
                .setUnbreakable(true)
                .setLore("&7Rarity: " + item.getRarity().getName());
        if (item.getDescription() != null) {
            builder.addLore("").addLore(StringUtil.wrap("&7" + item.getDescription(), 30));
        }
        return builder;
    }

}
