package me.ultradev.ultrarpg.api.mobs;

import me.ultradev.ultrarpg.api.combat.DamageInstance;
import me.ultradev.ultrarpg.api.util.ColorUtil;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.api.util.NumberUtil;
import me.ultradev.ultrarpg.game.mobs.CustomMob;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobInstance {

    private final CustomMob mob;
    private final Mob entity;

    private double health;
    private final Map<UUID, Double> damageMap = new HashMap<>();

    public MobInstance(CustomMob mob, Mob entity, double health) {
        this.mob = mob;
        this.entity = entity;
        this.health = health;
    }

    public CustomMob getMob() {
        return mob;
    }

    public Mob getEntity() {
        return entity;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double value) {
        this.health = value;
    }

    public Map<UUID, Double> getDamageMap() {
        return damageMap;
    }

    public void updateName() {
        boolean shorten = mob.getHealth() >= 100000;
        entity.setCustomName(ColorUtil.toColor("&8[&c\u2620&8] &c" + mob.getName() + " " +
                (health / mob.getHealth() <= 0.5 ? "&e" : "&a") +
                (shorten ? NumberUtil.toShortNumber((int) Math.ceil(health)) : (int) Math.ceil(health)) + "&7/&a" +
                (shorten ? NumberUtil.toShortNumber(mob.getHealth()) : mob.getHealth())));
    }

    public void rollDrops(DamageInstance data) {
        for (Map.Entry<UUID, Double> entry : damageMap.entrySet()) {
            Player bukkitPlayer = Bukkit.getPlayer(entry.getKey());
            if (bukkitPlayer == null || !bukkitPlayer.isOnline()) continue;
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            for (MobDrop drop : mob.getDrops()) {
                double luck = data.getAttackType().equals(DamageInstance.Type.MELEE) ? player.getStat(Stat.LUCK) :
                        NBTEditor.getDouble(data.getProjectile(), "stat.luck");
                double chance = drop.getChance() * (entry.getValue() / mob.getHealth()) * (1 + (luck / 100));
                if (NumberUtil.rollChance(chance)) {
                    for (int i = 0; i < drop.getAmount(); i++) {
                        player.giveItem(drop.getItem());
                    }
                    String message = drop.getItem().getRarity().getColorCode() +
                            (drop.getAmount() > 1 ? drop.getAmount() + "x " : "") +
                            drop.getItem().getName() + " &7(" + NumberUtil.formatDecimal(drop.getChance()) + "% chance)";
                    if (drop.getChance() <= 0.01) {
                        player.sendMessage("&c&lINSANELY RARE DROP! " + message);
                    } else if (drop.getChance() <= 0.1) {
                        player.sendMessage("&3&lINCREDIBLY RARE DROP! " + message);
                    } else if (drop.getChance() <= 1) {
                        player.sendMessage("&9&lVERY RARE DROP! " + message);
                    } else if (drop.getChance() <= 10) {
                        player.sendMessage("&b&lRARE DROP! " + message);
                    }
                    player.playNBS("rare_drop");
                }
            }
        }
    }

}
