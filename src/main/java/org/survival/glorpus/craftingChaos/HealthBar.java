package org.survival.glorpus.craftingChaos;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class HealthBar implements Listener {

    UUID following;
    UUID health_bar_id;

    Plugin plugin;

    public HealthBar(Plugin plugin, UUID followed_entity_id) {

        this.following = followed_entity_id;

        Location followed = getServer().getEntity(following).getLocation();

        this.plugin = plugin;

        Entity health_bar_entity = followed.getWorld().spawnEntity(followed, EntityType.TEXT_DISPLAY);
        health_bar_id = health_bar_entity.getUniqueId();
        TextDisplay health_bar = (TextDisplay) health_bar_entity;
        health_bar.setDisplayHeight(200);
        health_bar.setDisplayWidth(200);
        health_bar.setBillboard(Display.Billboard.CENTER);

        getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    double head_offset = 2.0;

    @EventHandler
    public void followEntity(ServerTickStartEvent start_tick) {
        try {

            Entity followed_entity = getServer().getEntity(this.following);
            TextDisplay health_bar = (TextDisplay) getServer().getEntity(this.health_bar_id);
            String boss_health = "";
            double health_percent = ((LivingEntity) followed_entity).getHealth() / ((LivingEntity) followed_entity).getMaxHealth();
            for (int i = 0; i < 10; i++) {
                if ( (double) i / health_percent < 10 ) {
                    boss_health = boss_health.concat("||");
                } else {
                    boss_health = boss_health.concat("..");
                }
            }
            health_bar.setText(boss_health);
            health_bar.teleport(followed_entity.getLocation().add(0.0, this.head_offset, 0.0));

        } catch (NullPointerException e) {
            getServer().getEntity(this.health_bar_id).remove();
            start_tick.getHandlers().unregister(this);
            getServer().getLogger().info("cleaned up health bar");
        }
    }

}
