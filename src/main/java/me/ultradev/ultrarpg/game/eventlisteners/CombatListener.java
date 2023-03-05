package me.ultradev.ultrarpg.game.eventlisteners;

import me.ultradev.ultrarpg.Main;
import me.ultradev.ultrarpg.api.mobs.MobInstance;
import me.ultradev.ultrarpg.api.util.NBTEditor;
import me.ultradev.ultrarpg.game.combat.CombatManager;
import me.ultradev.ultrarpg.game.player.ServerPlayer;
import me.ultradev.ultrarpg.game.stats.Stat;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CombatListener implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            if (!e.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.CUSTOM)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            if (!e.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)) {
                e.setDamage(0);
            }
        } else if (e.getEntity() instanceof Player bukkitPlayer) {
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            if (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                player.damage(e.getDamage() * 5);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                player.damage(player.getStat(Stat.HEALTH) / 5);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                player.damage(player.getStat(Stat.HEALTH) / 10);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                player.damage(player.getStat(Stat.HEALTH) / 15);
            }
            e.setDamage(0);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (NBTEditor.hasString(e.getEntity(), "mob_id")) {
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        Entity attacker = e.getDamager();
        if (ent instanceof Player bukkitPlayer) {
            e.setDamage(0);
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            MobInstance mobInstance = Main.getMobs().getOrDefault(attacker.getUniqueId(), null);
            if (mobInstance != null) player.damage(mobInstance);
        } else if (attacker instanceof Player bukkitPlayer) {
            ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
            MobInstance mobInstance = Main.getMobs().getOrDefault(ent.getUniqueId(), null);
            if (mobInstance != null) {
                double damage = CombatManager.calculateDamage(player, mobInstance);
                double newHealth = Math.max(0, mobInstance.getHealth() - damage);
                mobInstance.getDamageMap().put(player.getUniqueId(),
                        mobInstance.getDamageMap().getOrDefault(player.getUniqueId(), 0d) + Math.min(mobInstance.getHealth(), damage));
                if (newHealth == 0) {
                    mobInstance.setHealth(0);
                    mobInstance.updateName();
                    mobInstance.getEntity().setNoDamageTicks(0);
                    mobInstance.getEntity().damage(1);
                    Main.getMobs().remove(mobInstance.getEntity().getUniqueId());
                    mobInstance.rollDrops();
                } else {
                    mobInstance.setHealth(newHealth);
                    mobInstance.updateName();
                }
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            ServerPlayer player = ServerPlayer.fetch(e.getPlayer());
            int attackRange = NBTEditor.getInteger(player.getInventory().getItemInMainHand(), "attack_range");
            if (attackRange == 0) return;

            Location distLoc = player.getEyeLocation().add(player.getLocation().getDirection().normalize().multiply(attackRange));
            Vector vec = player.getEyeLocation().toVector().subtract(distLoc.toVector());
            vec.setX(vec.getX() * -1);
            vec.setY(vec.getY() * -1);
            vec.setZ(vec.getZ() * -1);

            RayTraceResult rayTrace = player.getWorld().rayTrace(player.getEyeLocation(), vec,
                    attackRange, FluidCollisionMode.NEVER, true, 0.4,
                    b -> !b.getType().equals(EntityType.PLAYER));

            if (rayTrace != null) {
                Entity entity = rayTrace.getHitEntity();
                if (entity != null) {
                    if (entity instanceof Mob mob && player.getLocation().distance(entity.getLocation()) > 3) {
                        mob.damage(player.getDamage(), player.get());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent e) {
        Entity ent = e.getEntity();
        if (ent instanceof Player bukkitPlayer) {
            Projectile proj = (Projectile) e.getProjectile();
            if (proj instanceof Arrow arrow) {
                if (e.getBow() == null) return;
                int extraArrows = NBTEditor.getInteger(e.getBow(), "arrows_shot") - 1;
                if (extraArrows >= 1) {
                    ServerPlayer player = ServerPlayer.fetch(bukkitPlayer);
                    int extraLeftArrows = (int) Math.floor(extraArrows / 2d);
                    for (int i = 0; i < extraLeftArrows; i++) {
                        Arrow newArrow = bukkitPlayer.getWorld().spawn(bukkitPlayer.getEyeLocation(), Arrow.class);
                        newArrow.setVelocity(arrow.getVelocity().rotateAroundY(Math.toRadians((i + 1) * (20d / extraLeftArrows))));
                        newArrow.setShooter(bukkitPlayer);
                        newArrow.setCritical(e.getForce() == 1);
                        CombatManager.applyArrowData(newArrow, e.getBow(), player);
                    }
                    int extraRightArrows = (int) Math.ceil(extraArrows / 2d);
                    for (int i = 0; i < extraRightArrows; i++) {
                        Arrow newArrow = bukkitPlayer.getWorld().spawn(bukkitPlayer.getEyeLocation(), Arrow.class);
                        newArrow.setVelocity(arrow.getVelocity().rotateAroundY(Math.toRadians((i + 1) * (-20d / extraLeftArrows))));
                        newArrow.setShooter(bukkitPlayer);
                        newArrow.setCritical(e.getForce() == 1);
                        CombatManager.applyArrowData(newArrow, e.getBow(), player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjHit(ProjectileHitEvent e) {
        e.getEntity().remove();
        if (e.getHitEntity() != null) {

        }
    }

}
