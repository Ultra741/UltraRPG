package me.ultradev.ultrarpg.api.util;

import org.bukkit.command.CommandSender;

public class MessageSender {

    private MessageSender() {}

    public static void chat(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.toColor(message));
    }

}
