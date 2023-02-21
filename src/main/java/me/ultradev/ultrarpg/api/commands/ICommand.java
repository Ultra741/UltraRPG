package me.ultradev.ultrarpg.api.commands;

@FunctionalInterface
public interface ICommand {
    void onCommand(CommandContext context);
}
