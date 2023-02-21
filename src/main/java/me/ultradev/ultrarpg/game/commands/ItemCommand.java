package me.ultradev.ultrarpg.game.commands;

import me.ultradev.ultrarpg.api.commands.Command;
import me.ultradev.ultrarpg.api.commands.CommandContext;
import me.ultradev.ultrarpg.api.commands.ICommand;
import me.ultradev.ultrarpg.items.GameItem;

@Command(label = "item", opOnly = true, arguments = {"[ITEM] [amount]"})
public class ItemCommand implements ICommand {

    @Override
    public void onCommand(CommandContext context) {
        context.asPlayer(player -> {
            String[] args = context.args();
            if (args.length < 1) {
                context.syntaxError("/item [item] (amount)");
                return;
            }

            String id = args[0].toUpperCase();
            try {
                int amount = 1;
                if (args.length >= 2) {
                    amount = Math.min(2304, Integer.parseInt(args[1]));
                    if (amount < 1) {
                        player.sendMessage("&cInvalid amount!");
                        return;
                    }
                }
                GameItem item = GameItem.valueOf(id);
                for (int i = 0; i < amount; i++) {
                    player.giveItem(item);
                }
                player.sendMessage("&aGave " + player.getName() + " item &e" + id + "&a.");
            } catch (NumberFormatException e) {
                player.sendMessage("&cInvalid amount!");
            } catch (IllegalArgumentException e) {
                player.sendMessage("&cInvalid item id!");
            }
        });
    }

}
