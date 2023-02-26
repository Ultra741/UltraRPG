package me.ultradev.ultrarpg.api.commands;

import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.api.util.MessageSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public record CommandContext(CommandSender sender, String[] args) {

    public void asPlayer(Consumer<ServerPlayer> consumer) {
        if (sender instanceof Player player) {
            consumer.accept(ServerPlayer.fetch(player));
        } else {
            MessageSender.chat(sender, "&cYou must be a player to do this!");
        }
    }

    public void syntaxError(String usage) {
        MessageSender.chat(sender, "&cMissing arguments! Usage: " + usage);
    }
}
