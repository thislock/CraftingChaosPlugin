package org.survival.glorpus.craftingChaos;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import static org.bukkit.Bukkit.getServer;

// this just adds a bunch of ways to craft things without a crafting table
public class AlternateCrafting implements Listener {

    Plugin plugin;

    // block lists
    Vector<Material> wood_logs = new Vector<>();
    Vector<Material> dirt = new Vector<>();
    Vector<Material> ores = new Vector<>();
    Vector<Material> ores_deepslate = new Vector<>();

    // tools
    Vector<Material> axes = new Vector<>();
    Vector<Material> pickaxes = new Vector<>();
    Vector<Material> shovels = new Vector<>();
    Vector<Material> hoes = new Vector<>();

    // custom items
    ItemStack pebble_axe;
    ItemStack pebble_shovel;
    ItemStack pebble_pickaxe;

    CustomItems custom_items;

    public AlternateCrafting(CraftingChaos plugin) {

        // wood logs
        wood_logs.add(Material.OAK_LOG);
        wood_logs.add(Material.SPRUCE_LOG);
        wood_logs.add(Material.BIRCH_LOG);
        wood_logs.add(Material.JUNGLE_LOG);
        wood_logs.add(Material.ACACIA_LOG);
        wood_logs.add(Material.DARK_OAK_LOG);
        wood_logs.add(Material.MANGROVE_LOG);
        wood_logs.add(Material.CHERRY_LOG);
        wood_logs.add(Material.PALE_OAK_LOG);

        // dirt
        dirt.add(Material.DIRT);
        dirt.add(Material.COARSE_DIRT);
        dirt.add(Material.ROOTED_DIRT);
        dirt.add(Material.DIRT_PATH);
        dirt.add(Material.GRASS_BLOCK);

        // stone ores
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.COPPER_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.COAL_ORE);
        // deepslate ores
        ores_deepslate.add(Material.DEEPSLATE_DIAMOND_ORE);
        ores_deepslate.add(Material.DEEPSLATE_EMERALD_ORE);
        ores_deepslate.add(Material.DEEPSLATE_REDSTONE_ORE);
        ores_deepslate.add(Material.DEEPSLATE_IRON_ORE);
        ores_deepslate.add(Material.DEEPSLATE_GOLD_ORE);
        ores_deepslate.add(Material.DEEPSLATE_COPPER_ORE);
        ores_deepslate.add(Material.DEEPSLATE_LAPIS_ORE);
        ores_deepslate.add(Material.DEEPSLATE_COAL_ORE);
        // axes
        axes.add(Material.NETHERITE_AXE);
        axes.add(Material.DIAMOND_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.GOLDEN_AXE);
        axes.add(Material.WOODEN_AXE);
        // hoes
        hoes.add(Material.NETHERITE_HOE);
        hoes.add(Material.DIAMOND_HOE);
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.WOODEN_HOE);
        // shovels
        shovels.add(Material.NETHERITE_SHOVEL);
        shovels.add(Material.DIAMOND_SHOVEL);
        shovels.add(Material.IRON_SHOVEL);
        shovels.add(Material.GOLDEN_SHOVEL);
        shovels.add(Material.STONE_SHOVEL);
        shovels.add(Material.WOODEN_SHOVEL);
        // pickaxes
        pickaxes.add(Material.NETHERITE_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.GOLDEN_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.WOODEN_PICKAXE);

        this.plugin = plugin;

        this.custom_items = new CustomItems(this.plugin);

        pebble_axe = custom_items.pebbleAxe();
        pebble_shovel = custom_items.pebbleShovel();
        pebble_pickaxe = custom_items.pebblePickAxe();

    }

    // when your killed by a boss, it sets keep inventory to true, this will just disable it right afterwards
    @EventHandler
    public void clearKeepInventory(PlayerDeathEvent e) {
        if (true) {
            e.setKeepInventory(false);
        }
    }

    MoreBosses boss_spawner = new MoreBosses();

    private long dirtGremlinGemTimer = System.currentTimeMillis();
    boolean broken_hold = false;
    @EventHandler
    public void dirtGremlinGemSummon(PlayerMoveEvent player_move) {
        Player player = player_move.getPlayer();
        ItemStack held_item = player.getInventory().getItemInMainHand();
        if (this.custom_items.isCustomItem(held_item, this.custom_items.dirt_gremlin_gem_id)) {

            if (System.currentTimeMillis() - dirtGremlinGemTimer == (1000 * 5) / 2) {
                player.sendMessage("you can feel the gem shaking...");
            }

            if (System.currentTimeMillis() - dirtGremlinGemTimer > 1000 * 3) {
                player.getInventory().getItemInMainHand().subtract();
                player.sendMessage("YOU HAVE SUMMONED A DIRT GREMLIN");
                dirtGremlinGemTimer = System.currentTimeMillis();

                boss_spawner.spawn_dirt_gremlin(plugin, player);

            }

        } else {
            dirtGremlinGemTimer = System.currentTimeMillis();
        }

    }

    public void spawnBossGemItems(Location location) {
        Random rand = new Random();
        switch (rand.nextInt(5)) {
            case 0:
                location.getWorld().dropItemNaturally(location, custom_items.dirtGremlinGem());
                break;
            case 1:
                break;
        }
    }

    Random random = new Random();

    @EventHandler
    public void breakDirtFist(BlockBreakEvent break_event) {
        // if dirt is broken with fist
        if (this.dirt.contains(break_event.getBlock().getType()) && break_event.getPlayer().getInventory().getItemInMainHand().isEmpty()) {
            if (random.nextInt(10) == 1) {
                break_event.getBlock().getWorld().dropItemNaturally(break_event.getBlock().getLocation(), this.pebble_shovel);
            }
        }
    }

    @EventHandler
    public void rollPlayerBreak(BlockBreakEvent e) {
        this.rollBossGem(e.getBlock());
    }

    public void rollBossGem(Block block_broken) {
        // checks if the block mined is an ore
        Material block_harvested = block_broken.getType();
        if (this.ores.contains(block_harvested) || this.ores_deepslate.contains(block_harvested)) {
            Location item_spawn_location = block_broken.getLocation();
            this.spawnBossGemItems(item_spawn_location);
        }
    }

    public void rollPebbleTool(Block interacted_dirt) {
        interacted_dirt.setType(Material.AIR);
        Location dirt_location = interacted_dirt.getLocation();

        Random rand = new Random();
        switch (rand.nextInt(15)) {
            case 0:
                dirt_location.getWorld().dropItemNaturally(dirt_location, this.pebble_axe);
                break;
            case 1:
                dirt_location.getWorld().dropItemNaturally(dirt_location, this.pebble_shovel);
                break;
            case 2:
                dirt_location.getWorld().dropItemNaturally(dirt_location, this.pebble_pickaxe);
                break;
        }
    }

    long interaction_delay = System.currentTimeMillis();
    @EventHandler
    public void diggyDirt(PlayerInteractEvent player_interact) {
        // if it's stripping a log
        if (player_interact.getAction().isRightClick()) {
            boolean block_empty = false;
            boolean item_in_hand = false;
            try {
                block_empty = !player_interact.getClickedBlock().isEmpty();
                item_in_hand = !player_interact.getItem().isEmpty();
            } catch (NullPointerException e) {
                block_empty = false;
                item_in_hand = false;
            }
            if (block_empty && item_in_hand) {
                // if the block clicked a is wood log
                if (this.dirt.contains(player_interact.getClickedBlock().getType())) {
                    // if the item in hand is an axe
                    if (this.shovels.contains(player_interact.getItem().getType())) {

                        long currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - this.interaction_delay;
                        if (elapsedTime > 100) {
                            this.interaction_delay = System.currentTimeMillis();

                            Block interacted_dirt = player_interact.getClickedBlock();
                            switch (interacted_dirt.getType()) {
                                case Material.DIRT:
                                    interacted_dirt.setType(Material.COARSE_DIRT);
                                    break;
                                case Material.COARSE_DIRT:
                                    interacted_dirt.setType(Material.ROOTED_DIRT);
                                    break;
                                case Material.DIRT_PATH:
                                    rollPebbleTool(interacted_dirt);
                                    break;
                                case Material.ROOTED_DIRT:
                                    rollPebbleTool(interacted_dirt);
                                    break;
                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void logStripping(PlayerInteractEvent player_interact) {

        // if it's stripping a log
        if (player_interact.getAction().isRightClick()) {
            boolean block_empty = false;
            boolean item_in_hand = false;
            try {
                block_empty = !player_interact.getClickedBlock().isEmpty();
                item_in_hand = !player_interact.getItem().isEmpty();
            } catch (NullPointerException e) {
                block_empty = false;
                item_in_hand = false;
            }
            if (block_empty && item_in_hand) {
                // if the block clicked a is wood log
                if (this.wood_logs.contains(player_interact.getClickedBlock().getType())) {
                    // if the item in hand is an axe
                    if (this.axes.contains(player_interact.getItem().getType())) {

                        // drop random wooden items

                        Random rand = new Random();

                        Location item_spawn_location = player_interact.getClickedBlock().getLocation();

                        for ( int i = 0; i < 4; i++ ) {

                            Material random_mat = Material.STICK;
                            switch (rand.nextInt(6)) {
                                case 0:
                                    random_mat = Material.OAK_PLANKS;
                                    break;
                                case 1:
                                    random_mat = Material.OAK_STAIRS;
                                    break;
                                case 2:
                                    random_mat = Material.OAK_SLAB;
                                    break;
                                case 3:
                                    // sticks
                                    break;
                                case 4:
                                    // sticks
                                    break;
                                case 5:
                                    //sticks
                                    break;
                            }

                            item_spawn_location.getWorld().dropItemNaturally(item_spawn_location, new ItemStack(random_mat));
                        }

                    }
                }
            }
        }
    }

}
