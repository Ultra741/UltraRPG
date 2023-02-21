package me.ultradev.ultrarpg.game.eventhandlers;

import me.ultradev.ultrarpg.api.player.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ServerPlayer sp = ServerPlayer.fetch(e.getPlayer());
    }

}
