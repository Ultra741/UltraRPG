package me.ultradev.ultrarpg;

import me.ultradev.ultrarpg.api.commands.CommandManager;
import me.ultradev.ultrarpg.api.commands.ICommand;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.api.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static final Map<UUID, ServerPlayer> PLAYERS = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        log("Registering listeners...");
        ReflectionUtil.findClasses("me.ultradev.ultrarpg.game", Listener.class)
                        .forEach(this::registerListener);

        log("Registering commands...");
        ReflectionUtil.findClasses("me.ultradev.ultrarpg.game.commands", ICommand.class)
                .forEach(this::registerCommand);

        log("Registering players...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("The server restarted!\nPlease rejoin.");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public static Main getInstance() {
        return instance;
    }

    public static void log(String s) {
        System.out.println("[UltraRPG] " + s);
    }

    public static Map<UUID, ServerPlayer> getPlayers() {
        return PLAYERS;
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(Class<? extends Listener> listener) {
        try {
            registerListener(listener.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerCommand(ICommand command) {
        CommandManager.getInstance().addCommand(command);
    }

    public void registerCommand(Class<? extends ICommand> command) {
        try {
            registerCommand(command.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
