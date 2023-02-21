package me.ultradev.ultrarpg.api.commands;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.util.MessageSender;
import me.ultradev.ultrarpg.items.GameItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager {

    private static CommandManager instance;

    private CommandManager() {
        instance = this;
    }

    public void addCommand(ICommand command) {
        Class<? extends ICommand> commandClass = command.getClass();
        Command[] annotations = commandClass.getAnnotationsByType(Command.class);

        if (annotations.length == 0) {
            throw new IllegalArgumentException("Couldn't find the @Command annotation in the " + command.getClass().getName() + " class!");
        }

        Command commandData = annotations[0];
        String label = commandData.label();
        boolean opOnly = commandData.opOnly();
        String[] aliases = commandData.aliases();

        org.bukkit.command.Command cmd = new org.bukkit.command.Command(label) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
                if (opOnly && !sender.isOp()) {
                    MessageSender.chat(sender, "&cYou must be an operator to do this!");
                    return true;
                }
                command.onCommand(new CommandContext(sender, args));
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
                return tabComplete(sender, alias, args, null);
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
                if (opOnly && !sender.isOp()) return new ArrayList<>();

                Set<String> arguments = new HashSet<>();
                for (String argument : commandData.arguments()) {
                    String[] all = argument.split(" ");
                    if (all.length >= args.length) {
                        boolean matches = true;
                        for (int i = 0; i < args.length - 1; i++) {
                            if ((!all[i].equalsIgnoreCase(args[i])) && !(all[i].startsWith("[") && all[i].endsWith("]"))) {
                                matches = false;
                                break;
                            }
                        }
                        if (!matches) continue;
                        String toCheck = all[args.length - 1];
                        if (toCheck.equalsIgnoreCase("[player]")) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                                    if (sender instanceof Player) {
                                        if (((Player) sender).canSee(player)) {
                                            arguments.add(player.getName());
                                        }
                                    }
                                }
                            }
                        } else if (toCheck.equalsIgnoreCase("[item]")) {
                            for (GameItem item : GameItem.values()) {
                                if (item.toString().startsWith(args[args.length - 1].toUpperCase())) {
                                    arguments.add(item.toString());
                                }
                            }
                        } else if (toCheck.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                            arguments.add(toCheck);
                        }
                    }
                }
                return arguments.stream().toList();
            }
        };

        if (opOnly) cmd.setPermission("groundcube.op");
        cmd.setAliases(Arrays.asList(aliases));

        CraftServer server = (CraftServer) Main.getInstance().getServer();
        SimpleCommandMap commandMap = server.getCommandMap();
        commandMap.register("groundcube", cmd);

    }

    public static CommandManager getInstance() {
        return instance == null ? new CommandManager() : instance;
    }

}
