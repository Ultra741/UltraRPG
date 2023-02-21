package me.ultradev.ultrarpg.game.eventlisteners;

import me.ultradev.ultrarpg.api.player.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ServerPlayer sp = ServerPlayer.fetch(e.getPlayer());
        sp.updateInventory();
    }

}
