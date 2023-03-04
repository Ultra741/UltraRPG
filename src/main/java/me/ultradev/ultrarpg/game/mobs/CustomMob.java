package me.ultradev.ultrarpg.game.mobs;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.mobs.MobEquipment;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.mobs.MobManager;
import me.ultradev.ultrarpg.api.util.Icon;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.mobs.undead.SkeletonSoldier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public enum CustomMob {

    SKELETON_SOLDIER(new SkeletonSoldier(), "Skeleton Soldier", EntityType.SKELETON,
            new MobEquipment(new Icon(Material.IRON_SWORD), null, new Icon(Material.GOLDEN_HELMET), new Icon(Material.CHAINMAIL_CHESTPLATE), null, null),
            30, 10),

    ;

    private final MobManager manager;

    private final String name;
    private final EntityType type;
    private final MobEquipment equipment;

    private final int health;
    private final double damage;

    CustomMob(MobManager manager, String name, EntityType type, MobEquipment equipment, int health, double damage) {
        this.manager = manager;
        this.name = name;
        this.type = type;
        this.equipment = equipment;
        this.health = health;
        this.damage = damage;

        if (manager != null) Main.getInstance().registerListener(manager);
    }

    public MobManager getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public MobEquipment getEquipment() {
        return equipment;
    }

    public int getHealth() {
        return health;
    }

    public double getDamage() {
        return damage;
    }

    public void spawn(Location location) {
        if (location.getWorld() == null) throw new IllegalArgumentException("Invalid spawn location!");
        Mob entity = (Mob) location.getWorld().spawnEntity(location, type);
        entity.setHealth(1);
        NBTEditor.addTag(entity, "mob_id", toString());
        if (equipment != null && entity.getEquipment() != null) {
            if (equipment.mainHand() != null) entity.getEquipment().setItemInMainHand(equipment.mainHand().toItemStack());
            if (equipment.offHand() != null) entity.getEquipment().setItemInOffHand(equipment.offHand().toItemStack());
            if (equipment.helmet() != null) entity.getEquipment().setHelmet(equipment.helmet().toItemStack());
            if (equipment.chestplate() != null) entity.getEquipment().setChestplate(equipment.chestplate().toItemStack());
            if (equipment.leggings() != null) entity.getEquipment().setLeggings(equipment.leggings().toItemStack());
            if (equipment.boots() != null) entity.getEquipment().setBoots(equipment.boots().toItemStack());
        }
        MobInstance instance = new MobInstance(this, entity, health);
        entity.setCustomNameVisible(true);
        instance.updateName();
        Main.getMobs().put(entity.getUniqueId(), instance);
    }

}
