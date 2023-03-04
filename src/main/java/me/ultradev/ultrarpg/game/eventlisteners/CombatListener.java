package me.ultradev.ultrarpg.game.eventlisteners;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.combat.CombatManager;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class CombatListener implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            if (!e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.CUSTOM)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            if (!e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) {
                e.setDamage(0);
            }
        } else if (e.getEntity() instanceof Player bukkitPlayer) {
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            e.setDamage(0);
            if (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)) {
                player.damage(e.getDamage() * 5);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        Entity attacker = e.getDamager();
        if (ent instanceof Player bukkitPlayer) {
            e.setDamage(0);
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            MobInstance mobInstance = Main.getMobs().getOrDefault(attacker.getUniqueId(), null);
            if (mobInstance != null) player.damage(mobInstance);
        } else if (attacker instanceof Player bukkitPlayer) {
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            MobInstance mobInstance = Main.getMobs().getOrDefault(ent.getUniqueId(), null);
            if (mobInstance != null) {
                double damage = CombatManager.calculateDamage(player, mobInstance);
                double newHealth = mobInstance.getHealth() - damage;
                if (newHealth <= 0) {
                    mobInstance.setHealth(0);
                    mobInstance.updateName();
                    mobInstance.getEntity().damage(1);
                    Main.getMobs().remove(mobInstance.getEntity().getUniqueId());
                } else {
                    mobInstance.setHealth(newHealth);
                    mobInstance.updateName();
                }
            }
        }
    }

}
