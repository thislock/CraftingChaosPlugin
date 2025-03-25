package org.survival.glorpus.craftingChaos;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class BossMove {

    // will attempt to smoothly move the entity to this location
    public Location targetPosition(Location target, Location current_pos, double speed) {

        double dx = target.getX() - current_pos.getX();
        double dy = target.getY() - current_pos.getY();
        double dz = target.getZ() - current_pos.getZ();

        // Calculate distance to target
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        // Avoid division by zero
        if (distance == 0) return current_pos;

        // Normalize direction vector
        double nx = dx / distance;
        double ny = dy / distance;
        double nz = dz / distance;

        // Move by speed, but don't overshoot the target
        double moveX = nx * Math.min(speed, distance);
        double moveY = ny * Math.min(speed, distance);
        double moveZ = nz * Math.min(speed, distance);

        Vector addVector = new Vector(moveX, moveY, moveZ);
        return current_pos.add(addVector);
    }

    Random random = new Random();

    public Location getRandomPosition(Location loc, int radius) {

        int randX = random.nextInt(radius);
        int randZ = random.nextInt(radius);

        double newX = loc.getX() + ( randX - (randX * 2.0) );
        double newZ = loc.getZ() + ( randZ - (randZ * 2.0) );

        Location location = new Location(loc.getWorld(), newX, 0.0, newZ);
        location.setY(location.getWorld().getHighestBlockYAt(location) + 2.0);

        return location;
    }


    boolean moving = false;

    int move_step = 0;

    private void start() {
        moving = true;
        move_step = 0;
    }

    private void end() {
        moving = false;
        move_step = 0;
    }

    Location target_position;
    Location starting_position;

    long start_time = System.currentTimeMillis();

    public boolean wait(Entity boss, long millis_waited) {
        if (!moving) {
            start_time = System.currentTimeMillis();
            this.start();
        }

        // wait 5 seconds
        if (start_time - System.currentTimeMillis() > millis_waited) {
            this.end();
            return true;
        }

        return false;
    }

    public boolean throwSpeed(Entity boss) {
        Entity potion_entity = boss.getWorld().spawnEntity(boss.getLocation().add(0, 1.0, 0), EntityType.POTION);

        ItemStack speed_potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potion_meta = (PotionMeta) speed_potion.getItemMeta();
        potion_meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 2, 5, false, true, true), true);
        speed_potion.setItemMeta(potion_meta);

        ((ThrownPotion) potion_entity).setItem(speed_potion);
        return true;
    }

    public boolean throwHeal(Entity boss) {
        Entity potion_entity = boss.getWorld().spawnEntity(boss.getLocation().add(0, 1.0, 0), EntityType.POTION);

        ItemStack speed_potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potion_meta = (PotionMeta) speed_potion.getItemMeta();
        potion_meta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 2, 1, false, true, true), true);
        speed_potion.setItemMeta(potion_meta);

        ((ThrownPotion) potion_entity).setItem(speed_potion);
        return true;
    }

    public boolean throwInstantDamage(Entity boss) {
        Entity potion_entity = boss.getWorld().spawnEntity(boss.getLocation().add(0, 1.0, 0), EntityType.POTION);

        ItemStack speed_potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potion_meta = (PotionMeta) speed_potion.getItemMeta();
        potion_meta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 2, 0, false, true, true), true);
        speed_potion.setItemMeta(potion_meta);

        ((ThrownPotion) potion_entity).setItem(speed_potion);
        return true;
    }

    public boolean leapRandom(Entity boss, Player player, double speed) {

        if (!moving) {
            starting_position = boss.getLocation();
            target_position = getRandomPosition(player.getLocation(), 8);
            this.start();
        }

        Location current_position = boss.getLocation();
        Location new_location;

        // leap forward
        new_location = targetPosition(target_position, current_position, speed);

        if (current_position.distance(target_position) < 1.0) {
            this.end();
            return true;
        }

        boss.teleport(new_location);

        return false;

    }

    // bool determines finished status, false means unfinished
    public boolean leapBomb(Entity boss, Player player, double speed) {

        if (!moving) {
            starting_position = boss.getLocation();
            target_position = player.getLocation();

            this.start();
        }

        Location current_position = boss.getLocation();
        Location new_location;


        if (this.move_step == 0) {
            // leap forward
            new_location = targetPosition(target_position, current_position, speed);
            boss.teleport(new_location);

            if (current_position.distance(target_position) < 1.0) {
                starting_position.setY(starting_position.getWorld().getHighestBlockYAt(starting_position) + 3.0);

                Location location_swap = this.starting_position;
                this.starting_position = this.target_position;
                this.target_position = location_swap;

                player.getWorld().spawnEntity(boss.getLocation(), EntityType.TNT);

                // go to the next step
                this.move_step = 1;
            }
        } else if (this.move_step == 1) {
            // leap back
            new_location = targetPosition(target_position, current_position, speed);
            boss.teleport(new_location);
            if (current_position.distance(target_position) < 1.0) {
                end();
                return true;
            }
        }

        return false;
    }

}
