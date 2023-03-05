package me.ultradev.ultrarpg.game.combat;

import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;

public class CombatManager {

    public static double calculateDamage(MobInstance mobInstance, ServerPlayer victim) {
        double defense = victim.getStat(Stat.DEFENSE);
        return mobInstance.getMob().getDamage() * (1 - (defense / (defense + 100)));
    }

    public static double calculateDamage(ServerPlayer player, MobInstance victim) {
        return player.getDamage() * (1 + (player.getStat(Stat.POWER) / 100));
    }

    public static void applyArrowData(Arrow arrow, ItemStack bow, ServerPlayer player) {
        int bowDamage = NBTEditor.getInteger(bow, "bow_damage");
        NBTEditor.addTag(arrow, "arrow_damage", bowDamage == 0 ? 1 : bowDamage);
        NBTEditor.addTag(arrow, "stat.power", player.getStat(Stat.POWER));
        NBTEditor.addTag(arrow, "stat.luck", player.getStat(Stat.LUCK));
    }

}
