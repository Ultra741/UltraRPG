package me.ultradev.ultrarpg.api.util;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private ColorUtil() {}

    public static final Pattern COLOR = Pattern.compile("&[0-9a-fA-F](&[k-rK-R])?");

    public static String toColor(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String stripColor(String s) {
        return s.replaceAll("&[0-9a-fA-Fk-orK-OR]", "");
    }

    @Nullable
    public static String getFirstColor(String input) {
        Matcher matcher = COLOR.matcher(input);
        String lastMatch = null;
        if (matcher.find()) {

        } else return null;
        while (matcher.find()) {
            lastMatch = matcher.group(1);
        }
        return lastMatch;
    }

    @Nullable
    public static String getLastColor(String input) {
        Matcher matcher = COLOR.matcher(input);
        String lastMatch = null;
        while (matcher.find()) {
            lastMatch = matcher.group();
        }
        return lastMatch;
    }

}
