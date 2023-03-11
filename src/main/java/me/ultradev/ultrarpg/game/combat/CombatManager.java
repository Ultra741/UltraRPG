package me.ultradev.ultrarpg.game.combat;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.combat.DamageInstance;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class CombatManager {

    public static double calculateDamage(MobInstance mobInstance, ServerPlayer victim) {
        double defense = victim.getStat(Stat.DEFENSE);
        return mobInstance.getMob().getDamage() * (1 - (defense / (defense + 100)));
    }

    public static void applyArrowData(Arrow arrow, ItemStack bow, ServerPlayer player) {
        int bowDamage = NBTEditor.getInteger(bow, "bow_damage");
        NBTEditor.addTag(arrow, "arrow_damage", bowDamage == 0 ? 1 : bowDamage);
        NBTEditor.addTag(arrow, "stat.power", player.getStat(Stat.POWER));
        NBTEditor.addTag(arrow, "stat.luck", player.getStat(Stat.LUCK));
    }

    public static void handlePlayerAttack(ServerPlayer player, Entity ent, DamageInstance data) {
        MobInstance mobInstance = Main.getMobs().getOrDefault(ent.getUniqueId(), null);
        if (mobInstance != null) {
            double playerDamage = data.getAttackType().equals(DamageInstance.Type.MELEE) ? player.getDamage() :
                    NBTEditor.getInteger(data.getProjectile(), "arrow_damage");
            double power = data.getAttackType().equals(DamageInstance.Type.MELEE) ? player.getStat(Stat.POWER) :
                    NBTEditor.getDouble(data.getProjectile(), "stat.power");
            double damage = playerDamage * (1 + (power / 100));
            double newHealth = Math.max(0, mobInstance.getHealth() - damage);
            mobInstance.getDamageMap().put(player.getUniqueId(),
                    mobInstance.getDamageMap().getOrDefault(player.getUniqueId(), 0d) + Math.min(mobInstance.getHealth(), damage));
            if (newHealth == 0) {
                mobInstance.setHealth(0);
                mobInstance.updateName();
                mobInstance.getEntity().setNoDamageTicks(0);
                mobInstance.getEntity().damage(1);
                Main.getMobs().remove(mobInstance.getEntity().getUniqueId());
                mobInstance.rollDrops(data);
            } else {
                mobInstance.setHealth(newHealth);
                mobInstance.updateName();
            }
        }
    }

}
