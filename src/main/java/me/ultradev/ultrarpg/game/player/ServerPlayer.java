package me.ultradev.ultrarpg.game.player;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.items.ItemInstance;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.stats.StatInstance;
import me.ultradev.ultrarpg.api.stats.StatSource;
import me.ultradev.ultrarpg.api.util.ColorUtil;
import me.ultradev.ultrarpg.api.util.ComponentBuilder;
import me.ultradev.ultrarpg.api.util.ItemBuilder;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.combat.CombatManager;
import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.stats.Stat;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerPlayer {

    public static ServerPlayer fetch(Player player) {
        if (Main.getPlayers().containsKey(player.getUniqueId())) {
            return Main.getPlayers().get(player.getUniqueId());
        }
        ServerPlayer serverPlayer = new ServerPlayer(player);
        Main.getPlayers().put(player.getUniqueId(), serverPlayer);
        new PlayerRunnable(serverPlayer).runTaskTimer(Main.getInstance(), 0, 1);
        return serverPlayer;
    }

    private final Player player;
    private final Map<Stat, StatInstance> stats = new HashMap<>();
    private double health;

    private ServerPlayer(Player player) {
        this.player = player;
        this.health = getStat(Stat.HEALTH);
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

    public Location getLocation() {
        return player.getLocation();
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public void sendMessage(String message) {
        player.sendMessage(ColorUtil.toColor(message));
    }

    public void sendDebugMessage(String message) {
        if (player.isOp()) sendMessage("&b[DEBUG] &f" + message);
    }

    public void sendActionbar(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).build());
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

    public List<ItemInstance> getArmorItems() {
        List<ItemInstance> result = new ArrayList<>();
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack armorPiece : armor) {
            if (armorPiece == null) {
                result.add(null);
                continue;
            }
            String id = NBTEditor.getString(armorPiece, "item_id");
            if (id.equals("")) result.add(null);
            else result.add(new ItemInstance(armorPiece));
        }
        return result;
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
                if (item.getItemMeta() == null) continue;
                if (NBTEditor.hasString(item, "item_id")) {
                    try {
                        ItemBuilder newItem = new ItemInstance(item).format().setAmount(item.getAmount());
                        player.getInventory().setItem(slot, newItem.build());
                    } catch (IllegalArgumentException e) {
                        player.getInventory().setItem(slot, new ItemStack(Material.AIR));
                        sendMessage("&cFailed to load one of your items! &8(" + NBTEditor.getString(item, "item_id") + ")");
                    }
                }
            }
            slot++;
        }
    }

    public Map<Stat, StatInstance> getStats() {
        return stats;
    }

    public double getStat(Stat stat) {
        if (!stats.containsKey(stat)) {
            Map<StatSource, Double> sources = new HashMap<>();
            sources.put(StatSource.BASE, stat.getBase());
            double value = stat.getBase();
            if (stat.getMax() != -1 && value > stat.getMax()) value = stat.getMax();
            stats.put(stat, new StatInstance(value, sources));
            return value;
        } else {
            return stats.get(stat).getValue();
        }
    }

    public Map<StatSource, Double> getStatSources(Stat stat) {
        if (!stats.containsKey(stat)) getStat(stat);
        return stats.get(stat).getSources();
    }

    public void updateStat(Stat stat, StatSource source, double newValue) {
        if (!stats.containsKey(stat)) getStat(stat);
        StatInstance instance = stats.get(stat);
        Map<StatSource, Double> sources = stats.get(stat).getSources();
        if (sources.containsKey(source)) {
            instance.setValue(instance.getValue() + (newValue - sources.put(source, newValue)));
        } else {
            sources.put(source, newValue);
            instance.setValue(instance.getValue() + newValue);
        }
    }

    public int getDamage() {
        int weaponDamage = NBTEditor.getInteger(player.getInventory().getItemInMainHand(), "weapon_damage");
        return weaponDamage == 0 ? 1 : weaponDamage;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
        updateVanillaHealth();
    }

    public void damage(MobInstance mobInstance) {
        health = Math.max(0, health - CombatManager.calculateDamage(mobInstance, this));
        checkDeath();
        updateVanillaHealth();
    }

    public void damage(double damage) {
        double defense = getStat(Stat.DEFENSE);
        health = Math.max(0, health - damage * (1 - (defense / (defense + 100))));
        checkDeath();
        updateVanillaHealth();
    }

    private void checkDeath() {
        if (health == 0) {
            health = getStat(Stat.HEALTH);
            player.teleport(player.getWorld().getSpawnLocation());
            sendMessage("&cYou died!");
        }
    }

    public void updateVanillaHealth() {
        player.setHealth(20 * (health / getStat(Stat.HEALTH)));
    }

}
