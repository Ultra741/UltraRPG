package me.ultradev.ultrarpg.api.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Icon {

    private final Material material;
    private final String headTexture;
    private final String hexColor;
    private final boolean glow;

    public static final List<Material> COLORABLE_MATERIALS = Arrays.asList(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.LEATHER_HORSE_ARMOR, Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION,
            Material.TIPPED_ARROW);

    public Icon(Material material, boolean glow) {
        this.material = material;
        this.headTexture = null;
        this.hexColor = null;
        this.glow = glow;
    }

    public Icon(String headTexture, boolean glow) {
        this.material = Material.PLAYER_HEAD;
        this.headTexture = headTexture;
        this.hexColor = null;
        this.glow = glow;
    }

    public Icon(Material material, String hexColor, boolean glow) {
        this.material = material;
        this.headTexture = null;
        this.hexColor = hexColor;
        this.glow = glow;
    }

    public Icon(Material material) {
        this(material, false);
    }

    public Icon(String headTexture) {
        this(headTexture, false);
    }

    public Icon(Material material, String hexColor) {
        this(material, hexColor, false);
    }

    public Material toMaterial() {
        return material;
    }

    public ItemBuilder toItemBuilder() {
        ItemBuilder builder = new ItemBuilder(material);
        if (material.equals(Material.PLAYER_HEAD) && headTexture != null) builder.setHeadTexture(headTexture);
        if (COLORABLE_MATERIALS.contains(material) && hexColor != null) {
            if (material.toString().startsWith("LEATHER_")) {
                builder.setArmorColor(hexColor);
            } else if (material.toString().endsWith("POTION") || material.equals(Material.TIPPED_ARROW)) {
                builder.setPotionColor(hexColor);
            }
        }
        if (glow) builder.makeGlow();
        return builder;
    }

    public ItemStack toItemStack() {
        return toItemBuilder().build();
    }

}
