package org.survival.glorpus.craftingChaos;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomItems {

    private Plugin plugin;
    NamespacedKey key;

    public CustomItems(Plugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(this.plugin, "custom_item_key");
    }

    private void setItemID(ItemMeta meta, int ID) {
        meta.getPersistentDataContainer().set(this.key, PersistentDataType.INTEGER, ID);
    }

    public int getItemID(ItemMeta meta) {
        try {
            return meta.getPersistentDataContainer().get(this.key, PersistentDataType.INTEGER);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public boolean isCustomItem(ItemStack item, int ID) {
        return this.getItemID(item.getItemMeta()) == ID;
    }

    int pebble_axe_id = 97898234;
    public ItemStack pebbleAxe() {
        ItemStack pebble_axe = new ItemStack(Material.STONE_AXE);

        ItemMeta pebble_axe_meta = pebble_axe.getItemMeta();

        pebble_axe_meta.setDisplayName("pebble axe");
        pebble_axe_meta.addAttributeModifier(
                Attribute.MINING_EFFICIENCY,
                    new AttributeModifier(new NamespacedKey(this.plugin, "chaos_pebble_axe_debuf"),
                            -2.0, AttributeModifier.Operation.ADD_NUMBER)
        );

        this.setItemID(pebble_axe_meta, this.pebble_axe_id);

        pebble_axe.setItemMeta(pebble_axe_meta);

        return pebble_axe;
    }

    int pebble_pickaxe_id = 28475374;
    public ItemStack pebblePickAxe() {
        ItemStack pebble_pickaxe = new ItemStack(Material.STONE_PICKAXE);
        {
            ItemMeta pebble_pickaxe_meta = pebble_pickaxe.getItemMeta();
            pebble_pickaxe_meta.setDisplayName("pebble pickaxe");
            pebble_pickaxe_meta.addAttributeModifier(
                    Attribute.MINING_EFFICIENCY,
                    new AttributeModifier(new NamespacedKey(this.plugin, "chaos_pebble_pick_debuf"),
                            -1.0, AttributeModifier.Operation.ADD_NUMBER)
            );
            this.setItemID(pebble_pickaxe_meta, pebble_pickaxe_id);
            pebble_pickaxe.setItemMeta(pebble_pickaxe_meta);
        }
        return pebble_pickaxe;
    }

    int pebble_shovel_id = 238475224;
    public ItemStack pebbleShovel() {
        ItemStack pebble_shovel = new ItemStack(Material.STONE_SHOVEL);
        {
            ItemMeta pebble_shovel_meta = pebble_shovel.getItemMeta();
            pebble_shovel_meta.setDisplayName("pebble shovel");
            pebble_shovel_meta.addAttributeModifier(
                    Attribute.MINING_EFFICIENCY,
                    new AttributeModifier(new NamespacedKey(this.plugin, "chaos_pebble_shovel_debuf"),
                            -1.0, AttributeModifier.Operation.ADD_NUMBER)
            );
            this.setItemID(pebble_shovel_meta, this.pebble_shovel_id);
            pebble_shovel.setItemMeta(pebble_shovel_meta);
        }
        return pebble_shovel;
    }

    int dirt_gremlin_gem_id = 2634557;
    public ItemStack dirtGremlinGem() {
        ItemStack dirt_gremlin_gem = new ItemStack(Material.EMERALD);

        ItemMeta dirt_gremlin_gem_meta = dirt_gremlin_gem.getItemMeta();
        dirt_gremlin_gem_meta.setDisplayName("Dirt Gremlin Gem");
        List<String> lore = new ArrayList<>();
        lore.add("a gem from a dirt gremlin");
        lore.add("holding it for too long might attract one...");
        dirt_gremlin_gem_meta.setLore(lore);

        this.setItemID(dirt_gremlin_gem_meta, dirt_gremlin_gem_id);

        dirt_gremlin_gem.setItemMeta(dirt_gremlin_gem_meta);
        return dirt_gremlin_gem;
    }

    int dirt_gremlin_helmet_id = 2847588;
    public ItemStack dirtGremlinHelmet() {

        ItemStack dirtGremlinHelmet = new ItemStack(Material.IRON_HELMET);

        ItemMeta helmetMeta = dirtGremlinHelmet.getItemMeta();
        helmetMeta.setDisplayName("Dirt Gremlin Helmet");
        helmetMeta.addEnchant(Enchantment.BLAST_PROTECTION, 6, true);

        dirtGremlinHelmet.setItemMeta(helmetMeta);
        return dirtGremlinHelmet;
    }

    int dynamite_glove_id = 1;
    public ItemStack dynamiteGlove() {

        ItemStack dynamiteGlove = new ItemStack(Material.CROSSBOW);

        ItemMeta glove_meta = dynamiteGlove.getItemMeta();
        glove_meta.setDisplayName("Dynamite Glove");
        setItemID(glove_meta, dynamite_glove_id);

        dynamiteGlove.setItemMeta(glove_meta);
        return dynamiteGlove;

    }

}
