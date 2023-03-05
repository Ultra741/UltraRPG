package me.ultradev.ultrarpg.api.mobs;

import me.ultradev.ultrarpg.game.items.GameItem;
import me.ultradev.ultrarpg.game.player.ServerPlayer;

import java.util.function.Predicate;

public class MobDrop {

    private final GameItem item;
    private final int amount;
    private final double chance;

    private Predicate<ServerPlayer> condition;

    public MobDrop(GameItem item, int amount, double chance) {
        this.item = item;
        this.amount = amount;
        this.chance = chance;
        condition = p -> true;
    }

    public MobDrop setCondition(Predicate<ServerPlayer> condition) {
        this.condition = condition;
        return this;
    }

    public GameItem getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public double getChance() {
        return chance;
    }

    public Predicate<ServerPlayer> getCondition() {
        return condition;
    }

}
