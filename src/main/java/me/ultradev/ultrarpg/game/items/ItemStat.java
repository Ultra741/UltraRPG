package me.ultradev.ultrarpg.game.items;

import me.ultradev.ultrarpg.game.stats.Stat;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ItemStats.class)
public @interface ItemStat {
    Stat stat();
    double value();
}
