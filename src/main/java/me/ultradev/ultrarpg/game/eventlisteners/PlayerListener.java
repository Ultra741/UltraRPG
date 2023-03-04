package me.ultradev.ultrarpg.game.eventlisteners;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ServerPlayer sp = ServerPlayer.fetch(e.getPlayer());
        sp.updateInventory();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Main.getPlayers().remove(e.getPlayer().getUniqueId());
    }

}
