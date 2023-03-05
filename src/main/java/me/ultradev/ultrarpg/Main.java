package me.ultradev.ultrarpg;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.ultradev.ultrarpg.api.commands.CommandManager;
import me.ultradev.ultrarpg.api.commands.ICommand;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.api.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static final Map<UUID, ServerPlayer> PLAYERS = new HashMap<>();
    private static final Map<UUID, MobInstance> MOBS = new HashMap<>();
    private static final Map<String, Song> NBS_FILES = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        ReflectionUtil.findClasses("me.ultradev.ultrarpg.game.eventlisteners", Listener.class)
                        .forEach(this::registerListener);
        ReflectionUtil.findClasses("me.ultradev.ultrarpg.game.commands", ICommand.class)
                .forEach(this::registerCommand);

        NBS_FILES.put("rare_drop", NBSDecoder.parse(new File(Main.getInstance().getDataFolder(), "rare_drop.nbs")));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("The server restarted!\nPlease rejoin.");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (NBTEditor.hasString(entity, "mob_id")) {
                entity.remove();
            }
        }

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

    public static Map<UUID, MobInstance> getMobs() {
        return MOBS;
    }

    public static Map<String, Song> getNbsFiles() {
        return NBS_FILES;
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
