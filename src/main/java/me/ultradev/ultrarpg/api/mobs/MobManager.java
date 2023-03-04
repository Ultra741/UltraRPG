package me.ultradev.ultrarpg.api.mobs;

import org.bukkit.event.Listener;

public abstract class MobManager implements Listener {

    public abstract void onSpawn();
    public abstract void onDeath();

}
