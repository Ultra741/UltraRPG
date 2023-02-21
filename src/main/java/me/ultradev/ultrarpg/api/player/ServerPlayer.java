package me.ultradev.ultrarpg.api.player;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemInstance;
import me.ultradev.ultrarpg.api.util.ColorUtil;
import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.items.GameItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class ServerPlayer {

    public static ServerPlayer fetch(Player player) {
        if (Main.getPlayers().containsKey(player.getUniqueId())) {
            return Main.getPlayers().get(player.getUniqueId());
        }
        ServerPlayer serverPlayer = new ServerPlayer(player);
        Main.getPlayers().put(player.getUniqueId(), serverPlayer);
        return serverPlayer;
    }

    private final Player player;

    public ServerPlayer(Player player) {
        this.player = player;
    }

    public Player get() {
        return player;
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public String getName() {
        return player.getName();
    }

    public void sendMessage(String message) {
        player.sendMessage(ColorUtil.toColor(message));
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public void giveItem(ItemInstance item) {
        player.getInventory().addItem(item.format().build());
    }

    public void giveItem(GameItem item) {
        giveItem(new ItemInstance(item));
    }

    public void updateInventory() {
        int slot = 0;
        while (slot <= 45) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null) {
                if (NBTEditor.hasString(item, "item_id")) {
                    ItemBuilder newItem = new ItemInstance(item).format().setAmount(item.getAmount());
                    if (!item.isSimilar(newItem.build())) {
                        player.getInventory().setItem(slot, newItem.build());
                        sendMessage("&aYour " + newItem.getName() + " &ahas been updated!");
                    }
                }
            }
            slot++;
        }
    }

}
