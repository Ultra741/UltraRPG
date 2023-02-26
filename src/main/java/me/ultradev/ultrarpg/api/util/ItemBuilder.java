package me.ultradev.ultrarpg.api.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder implements IBuilder<ItemStack>, Cloneable {

    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder editMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = item.getItemMeta();
        consumer.accept(meta);
        item.setItemMeta(meta);
        return this;
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }

    public List<String> getLore() {
        if (item.getItemMeta().hasLore()) {
            return item.getItemMeta().getLore();
        } else {
            return new ArrayList<>();
        }
    }

    public Material getMaterial() {
        return item.getType();
    }

    public int getAmount() {
        return item.getAmount();
    }

    public ItemMeta getMeta() {
        return item.getItemMeta();
    }

    public boolean hasTag(String key) {
        return NBTEditor.hasString(item, key);
    }

    public String getString(String key) {
        return NBTEditor.getString(item, key);
    }

    public int getInteger(String key) {
        return NBTEditor.getInteger(item, key);
    }

    public ItemBuilder setName(String name) {
        return editMeta(meta -> meta.setDisplayName(ColorUtil.toColor(name)));
    }

    public ItemBuilder setLore(List<String> lore) {
        lore.replaceAll(s -> ColorUtil.toColor(s.replaceAll("&r", "&7")));
        return editMeta(meta -> meta.setLore(lore));
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(new ArrayList<>(List.of(lore)));
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = getLore();
        lore.addAll(List.of(line.split("\n")));
        return setLore(lore);
    }

    public ItemBuilder addLore(String... lines) {
        List<String> lore = getLore();
        for (String line : lines) {
            lore.addAll(List.of(StringUtil.wrap(line, 35).split("\n")));
        }
        return setLore(lore);
    }

    public ItemBuilder setLoreLine(int index, String line) {
        List<String> lore = getLore();
        lore.set(index, line);
        return setLore(lore);
    }

    public ItemBuilder insertLoreLine(int index, String line) {
        List<String> lore = getLore();
        lore.add(index, line);
        return setLore(lore);
    }

    public ItemBuilder removeLoreLine(int index) {
        List<String> lore = getLore();
        lore.remove(index);
        return setLore(lore);
    }

    public ItemBuilder clearLore() {
        return editMeta(meta -> meta.setLore(new ArrayList<>()));
    }

    public ItemBuilder setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addItemTag(String key, String value) {
        item = NBTEditor.addTag(item, key, value);
        return this;
    }

    public ItemBuilder addItemTag(String key, int value) {
        item = NBTEditor.addTag(item, key, value);
        return this;
    }

    public ItemBuilder removeTag(String key) {
        item = NBTEditor.removeTag(item, key);
        return this;
    }

    public ItemBuilder hideFlags() {
        return editMeta(meta -> meta.addItemFlags(ItemFlag.values()));
    }

    public ItemBuilder setHeadTexture(String url) {
        if (!item.getType().equals(Material.PLAYER_HEAD)) return this;
        if (url.isEmpty()) return this;

        return editMeta(meta -> {
            SkullMeta headMeta = (SkullMeta) meta;
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", url));

            try {
                Field profileField = headMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(headMeta, profile);
            } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
                error.printStackTrace();
            }
        });
    }

    public ItemBuilder setHeadOwner(UUID uuid) {
        if (!item.getType().equals(Material.PLAYER_HEAD)) return this;
        return editMeta(meta -> {
            SkullMeta headMeta = (SkullMeta) meta;
            headMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        });
    }

    public ItemBuilder setArmorColor(String hex) {
        if (!item.getType().toString().startsWith("LEATHER_")) return this;
        return editMeta(meta -> {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
            java.awt.Color color = java.awt.Color.decode(hex);
            armorMeta.setColor(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
        });
    }

    public ItemBuilder setPotionColor(String hex) {
        if (!item.getType().toString().endsWith("POTION")
                && !item.getType().equals(Material.TIPPED_ARROW)) return this;
        return editMeta(meta -> {
            PotionMeta potionMeta = (PotionMeta) meta;
            java.awt.Color color = java.awt.Color.decode(hex);
            potionMeta.setColor(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
        });
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder makeGlow() {
        if (item.getType().equals(Material.BOW)) addEnchantment(Enchantment.DIG_SPEED, 1);
        else addEnchantment(Enchantment.ARROW_INFINITE, 1);
        return editMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
    }

    public ItemBuilder addAttribute(Attribute attribute, double amount, EquipmentSlot slot) {
        return editMeta(meta -> meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), "", amount, AttributeModifier.Operation.ADD_NUMBER, slot)));
    }

    public ItemBuilder setUnbreakable(boolean value) {
        return editMeta(meta -> meta.setUnbreakable(value));
    }

    @Override
    public ItemStack build() {
        return item.clone();
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.item.clone());
    }

}
