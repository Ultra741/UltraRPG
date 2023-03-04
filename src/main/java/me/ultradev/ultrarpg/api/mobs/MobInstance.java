package me.ultradev.ultrarpg.api.mobs;

import me.ultradev.ultrarpg.api.util.ColorUtil;
import me.ultradev.ultrarpg.api.util.NumberUtil;
import me.ultradev.ultrarpg.game.mobs.CustomMob;
import org.bukkit.entity.Mob;

public class MobInstance {

    private final CustomMob mob;
    private final Mob entity;

    private double health;

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

    public void updateName() {
        boolean shorten = mob.getHealth() >= 100000;
        entity.setCustomName(ColorUtil.toColor("&8[&c\u2620&8] &c" + mob.getName() + " " +
                (health / mob.getHealth() <= 0.5 ? "&e" : "&a") +
                (shorten ? NumberUtil.toShortNumber((int) Math.ceil(health)) : (int) Math.ceil(health)) + "&7/&a" +
                (shorten ? NumberUtil.toShortNumber(mob.getHealth()) : mob.getHealth())));
    }

}
