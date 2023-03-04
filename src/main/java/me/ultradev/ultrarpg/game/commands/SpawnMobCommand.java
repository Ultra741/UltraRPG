package me.ultradev.ultrarpg.game.commands;

import me.ultradev.ultrarpg.api.commands.Command;
import me.ultradev.ultrarpg.api.commands.CommandContext;
import me.ultradev.ultrarpg.api.commands.ICommand;
import me.ultradev.ultrarpg.game.mobs.CustomMob;

@Command(label = "spawnmob", opOnly = true, arguments = {"[MOB]"})
public class SpawnMobCommand implements ICommand {

    @Override
    public void onCommand(CommandContext context) {
        context.asPlayer(player -> {
            String[] args = context.args();
            if (args.length != 1) {
                context.syntaxError("/spawnmob [mob]");
                return;
            }

            try {
                CustomMob mob = CustomMob.valueOf(args[0].toUpperCase());
                mob.spawn(player.getLocation());
                player.sendMessage("&aSpawned mob &e" + mob + "&a.");
            } catch (IllegalArgumentException e) {
                player.sendMessage("&cInvalid mob id!");
            }
        });
    }

}
