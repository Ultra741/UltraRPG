package me.ultradev.ultrarpg.api.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ComponentBuilder implements IBuilder<TextComponent> {

    private final TextComponent component;

    public ComponentBuilder(String message, String clickCommand, ClickEvent.Action clickAction, String hoverMessage) {
        component = new TextComponent(ColorUtil.toColor(message));
        if (!clickCommand.equals("")) component.setClickEvent(new ClickEvent(clickAction, clickCommand));
        if (!hoverMessage.equals("")) component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ColorUtil.toColor(hoverMessage))));
    }

    public ComponentBuilder(String message, String hoverMessage) {
        this(message, "", ClickEvent.Action.RUN_COMMAND, hoverMessage);
    }

    public ComponentBuilder(String message, String clickCommand, String hoverMessage) {
        this(message, clickCommand, ClickEvent.Action.RUN_COMMAND, hoverMessage);
    }

    public ComponentBuilder(String message) {
        this(message, "", ClickEvent.Action.RUN_COMMAND, "");
    }

    @Override
    public TextComponent build() {
        return component;
    }

}
