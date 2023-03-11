package me.ultradev.ultrarpg.api.combat;

import org.bukkit.entity.Projectile;

public class DamageInstance {

    private final Type attackType;

    private Projectile projectile;

    public DamageInstance(Type attackType) {
        this.attackType = attackType;
    }

    public DamageInstance projectile(Projectile projectile) {
        this.projectile = projectile;
        return this;
    }

    public Type getAttackType() {
        return attackType;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public enum Type {
        MELEE,
        BOW
    }

}
