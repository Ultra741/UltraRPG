package me.ultradev.ultrarpg.api.util;

import me.ultradev.ultrarpg.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTEditor {

    private NBTEditor() {}

    public static ItemStack addTag(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addTag(ItemStack item, String key, int value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addTag(ItemStack item, String key, double value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
        return item;
    }

    public static String getString(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return "";
        return meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, "");
    }

    public static int getInteger(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;
        return meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER, 0);
    }

    public static double getDouble(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;
        return meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.getInstance(), key), PersistentDataType.DOUBLE, 0d);
    }

    public static ItemStack removeTag(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().remove(new NamespacedKey(Main.getInstance(), key));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean hasString(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        else return meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING);
    }

    public static boolean hasInteger(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        else return meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER);
    }

    public static boolean hasDouble(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        else return meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.DOUBLE);
    }

    public static void addTag(Entity entity, String key, String value) {
        entity.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, value);
    }

    public static void addTag(Entity entity, String key, int value) {
        entity.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER, value);
    }

    public static String getString(Entity entity, String key) {
        return entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING, "");
    }

    public static int getInteger(Entity entity, String key) {
        return entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER, 0);
    }

    public static void removeTag(Entity entity, String key) {
        entity.getPersistentDataContainer().remove(new NamespacedKey(Main.getInstance(), key));
    }

    public static boolean hasString(Entity entity, String key) {
        return entity.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.STRING);
    }

    public static boolean hasInteger(Entity entity, String key) {
        return entity.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), key), PersistentDataType.INTEGER);
    }

}
