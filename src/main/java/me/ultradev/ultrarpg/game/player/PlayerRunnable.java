package me.ultradev.ultrarpg.game.player;

import me.ultradev.ultrarpg.api.items.ItemInstance;
import me.ultradev.ultrarpg.api.stats.StatSource;
import me.ultradev.ultrarpg.api.util.NumberUtil;
import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

public class PlayerRunnable extends BukkitRunnable {

    private final ServerPlayer player;
    private int i = 1;

    public PlayerRunnable(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        if (i % 5 == 0) {
            ItemInstance heldItem = player.getHeldItem();
            if (heldItem != null) {
                if (heldItem.getItem().getType().equals(GameItem.Type.WEAPON)) {
                    for (Map.Entry<Stat, Double> entry : heldItem.getStats().entrySet()) {
                        player.updateStat(entry.getKey(), StatSource.HELD_ITEM, entry.getValue());
                    }
                }
            } else {
                for (Stat stat : Stat.values()) {
                    player.updateStat(stat, StatSource.HELD_ITEM, 0);
                }
            }

            List<ItemInstance> armor = player.getArmorItems();
            for (int i = 0; i < armor.size(); i++) {
                ItemInstance armorPiece = player.getArmorItems().get(i);
                if (armorPiece != null) {
                    StatSource source = switch (armorPiece.getItem().getSubType()) {
                        case HELMET -> StatSource.EQUIPPED_HELMET;
                        case CHESTPLATE -> StatSource.EQUIPPED_CHESTPLATE;
                        case LEGGINGS -> StatSource.EQUIPPED_LEGGINGS;
                        default -> StatSource.EQUIPPED_BOOTS;
                    };
                    for (Map.Entry<Stat, Double> entry : armorPiece.getStats().entrySet()) {
                        player.updateStat(entry.getKey(), source, entry.getValue());
                    }
                } else {
                    StatSource source = switch (i) {
                        case 0 -> StatSource.EQUIPPED_BOOTS;
                        case 1 -> StatSource.EQUIPPED_LEGGINGS;
                        case 2 -> StatSource.EQUIPPED_CHESTPLATE;
                        default -> StatSource.EQUIPPED_HELMET;
                    };
                    for (Stat stat : Stat.values()) {
                        player.updateStat(stat, source, 0);
                    }
                }
            }

            double maxHealth = player.getStat(Stat.HEALTH);
            if (player.getHealth() > maxHealth) {
                player.setHealth(maxHealth);
            }

            player.get().setWalkSpeed((float) (0.2 + (player.getStat(Stat.SPEED) / 500f)));

            if (i % 40 == 0) {
                player.setHealth(Math.min(maxHealth, player.getHealth() + (maxHealth * 0.05)));
            }
            StringBuilder actionbar = new StringBuilder("&c" + NumberUtil.toFancyNumber(Math.ceil(player.getHealth())) + "/" + NumberUtil.toFancyNumber(maxHealth) + "\u2764");
            double defense = player.getStat(Stat.DEFENSE);
            if (defense > 0) actionbar.append(" &b").append(NumberUtil.toFancyNumber(defense)).append("\u26E8");
            player.sendActionbar(actionbar.toString());
        }

        i++;
    }

}
