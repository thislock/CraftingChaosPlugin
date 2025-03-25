package org.survival.glorpus.craftingChaos;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.w3c.dom.Text;

import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class Boss implements Listener {

    Plugin plugin;
    UUID entityID;

    HealthBar health_bar;

    Player focused_player;

    // all the different items they can drop
    java.util.Vector<ItemStack> loot_table = new java.util.Vector<>();
    AttackPatterns attack_patterns;

    public Boss(Plugin plugin, Player player, EntityType entity_type) {
        this.plugin = plugin;
        Random rand = new Random();
        Entity boss_entity = player.getWorld().spawnEntity(player.getLocation(), entity_type);
        entityID = boss_entity.getUniqueId();
        focused_player = player;
        getServer().getLogger().info("1");
        this.health_bar = new HealthBar(plugin, entityID);

        this.attack_patterns = new AttackPatterns(entityID, player);
    }

    public void setAttackPatterns(AttackPatterns patterns) {
        this.attack_patterns = patterns;
    }

    public void setLootTable(java.util.Vector<ItemStack> loot_table) {
        this.loot_table = loot_table;
    }

    public Entity get_entity() {
        return getServer().getEntity(this.entityID);
    }

    public void registerBoss() {
        getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    Random random = new Random();

    // intercepts the player death event if the boss killed them, just respawning them where they were and despawning the boss
    @EventHandler
    public void bossMercy(PlayerDeathEvent player_death) {
        try {
            if (player_death.getEntity().getUniqueId() == focused_player.getUniqueId()) {
                //player_death.getPlayer().sendMessage("because you were killed by a boss, your items have been spared...");
                // makes the player keep their inventory upon death
                player_death.setKeepInventory(true);
                // clears the items they drop to prevent duping
                player_death.getDrops().clear();

                getServer().getEntity(this.entityID).remove();
            }
        } catch (NullPointerException e) {

        }
    }

    @EventHandler
    public void bossTick(ServerTickStartEvent tick_start) {

        boolean just_died = false;

        // make sure the boss is still alive, and if it isn't, clean up the event handler
        try {
            // something impossible if the entity doesn't exist
            getServer().getEntity(this.entityID).getLocation();
            // that won't work if it just died, so check for that
            if (this.get_entity().isDead()) {
                // drop a random item from the loot table
                if (!this.loot_table.isEmpty()) {
                    get_entity().getLocation().getWorld().dropItemNaturally(get_entity().getLocation(), this.loot_table.get(random.nextInt(this.loot_table.size())));
                }
                just_died = true;
                clean_event(tick_start);
            }
        } catch (NullPointerException e) {
            just_died = true;
            clean_event(tick_start);
        }

        Entity boss_entity = getServer().getEntity(this.entityID);
        if (!just_died) {
            this.attack_patterns.patternTick();
        }

    }

    public void clean_event(ServerTickStartEvent tick_start) {
        tick_start.getHandlers().unregister(this);
        getServer().getLogger().info("boss tick has been cleaned");
    }

}
