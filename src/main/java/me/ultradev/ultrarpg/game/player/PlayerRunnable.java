package me.ultradev.ultrarpg.game.player;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerRunnable extends BukkitRunnable {

    private int i = 1;

    @Override
    public void run() {
        i++;
    }

}
