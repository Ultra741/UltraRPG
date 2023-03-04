package me.ultradev.ultrarpg.game.combat;

import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.game.stats.Stat;

public class CombatManager {

    public static double calculateDamage(MobInstance mobInstance, ServerPlayer victim) {
        double defense = victim.getStat(Stat.DEFENSE);
        return mobInstance.getMob().getDamage() * (1 - (defense / (defense + 100)));
    }

    public static double calculateDamage(ServerPlayer player, MobInstance victim) {
        return player.getDamage() * (1 + (player.getStat(Stat.POWER) / 100));
    }

}
