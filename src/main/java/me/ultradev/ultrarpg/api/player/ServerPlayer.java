package me.ultradev.ultrarpg.api.player;

import me.ultradev.ultrarpg.Main;
import org.bukkit.entity.Player;

public class ServerPlayer {

    public static ServerPlayer fetch(Player player) {
        if (Main.getPlayers().containsKey(player.getUniqueId())) return Main.getPlayers().get(player.getUniqueId());
        ServerPlayer serverPlayer = new ServerPlayer(player);
        Main.getPlayers().put(player.getUniqueId(), serverPlayer);
        return serverPlayer;
    }

    private final Player player;

    public ServerPlayer(Player player) {
        this.player = player;
    }

    public Player get() {
        return player;
    }

}
