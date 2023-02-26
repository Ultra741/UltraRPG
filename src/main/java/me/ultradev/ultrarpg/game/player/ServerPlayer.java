package me.ultradev.ultrarpg.game.player;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemInstance;
import me.ultradev.ultrarpg.api.util.ColorUtil;
import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerPlayer {

    public static ServerPlayer fetch(Player player) {
        if (Main.getPlayers().containsKey(player.getUniqueId())) {
            return Main.getPlayers().get(player.getUniqueId());
        }
        ServerPlayer serverPlayer = new ServerPlayer(player);
        Main.getPlayers().put(player.getUniqueId(), serverPlayer);
        new PlayerRunnable().runTaskTimer(Main.getInstance(), 0, 1);
        return serverPlayer;
    }

    private final Player player;
    private final Map<Stat, Double> stats = new HashMap<>();

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

    @Nullable
    public ItemInstance getHeldItem() {
        ItemStack item = player.getInventory().getItemInMainHand();
        String id = NBTEditor.getString(item, "item_id");
        if (id.equals("")) return null;
        else return new ItemInstance(item);
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
                    try {
                        ItemBuilder newItem = new ItemInstance(item).format().setAmount(item.getAmount());
                        if (!item.isSimilar(newItem.build())) {
                            player.getInventory().setItem(slot, newItem.build());
                            sendMessage("&aYour " + newItem.getName() + " &ahas been updated!");
                        }
                    } catch (IllegalArgumentException e) {
                        player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                        sendMessage("&cFailed to load one of your items! &8(" + NBTEditor.getString(item, "item_id") + ")");
                    }
                }
            }
            slot++;
        }
    }

    public Map<Stat, Double> getStats() {
        return stats;
    }

    public double getStat(Stat stat) {
        return stats.getOrDefault(stat, (double) stat.getBase());
    }

}
