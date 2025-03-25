package org.survival.glorpus.craftingChaos;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class CraftingListener implements Listener {

    Vector<Material> valid_mats = new Vector<>();
    HashMap<Material, Material> random_crafting_results = new HashMap<>();

    Plugin plugin;

    public CraftingListener(Plugin plugin) {

        for (Material mat : Material.values()) {
            if (mat.isItem()) {
                this.valid_mats.add(mat);
            }
        }

        this.plugin = plugin;
    }

    public Material get_random_mat() {
        Random random = new Random();
        return this.valid_mats.get(random.nextInt(this.valid_mats.size()));
    }

    public ItemStack scramble_tools(Material mat) {

        // if it's stackable, just ignore it, because i don't know how to filter tools and weapons lol
        if (mat.isItem()) {
            ItemStack test_item = new ItemStack(mat);
            if (test_item.getMaxStackSize() != 1) {
                return test_item;
            }
        }

        ItemStack craft_result = new ItemStack(mat);
        ItemMeta craft_meta = craft_result.getItemMeta();

        Random random = new Random();
        List<String> lore = new ArrayList<>();
        lore.add("this items blessing is - ");
        switch (random.nextInt(6)) {
            case 0:
                craft_meta.addAttributeModifier(
                        Attribute.ATTACK_DAMAGE,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_weak_weapon_attribute"),
                                -2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                craft_meta.addAttributeModifier(
                        Attribute.ARMOR,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_weak_armor_attribute"),
                                -2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                lore.add("weak");
                break;
            case 1:
                craft_meta.addAttributeModifier(
                        Attribute.ATTACK_KNOCKBACK,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_armor_negitave_knockback_attribute"),
                                2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                craft_meta.addAttributeModifier(
                        Attribute.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_negitave_knockback_attribute"),
                                -2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                lore.add("negative knockback");
                break;
            case 2:
                craft_meta.addAttributeModifier(
                        Attribute.GRAVITY,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_more_gravity_attribute"),
                                1.5, AttributeModifier.Operation.ADD_SCALAR)
                );
                lore.add("more gravity");
                break;
            case 3:
                craft_meta.addAttributeModifier(
                        Attribute.ATTACK_KNOCKBACK,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_positive_knockback_attribute"),
                                -1.0, AttributeModifier.Operation.ADD_SCALAR)
                );
                craft_meta.addAttributeModifier(
                        Attribute.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_armor_positive_knockback_attribute"),
                                2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                lore.add("positive knockback");
                break;
            case 4:
                craft_meta.addAttributeModifier(
                        Attribute.GRAVITY,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_gravity_attribute"),
                                -1.4, AttributeModifier.Operation.ADD_SCALAR)
                );
                lore.add("reverse gravity");
                break;
            case 5:
                craft_meta.addAttributeModifier(
                        Attribute.MAX_HEALTH,
                        new AttributeModifier(new NamespacedKey(this.plugin, "chaos_max_health"),
                                2.0, AttributeModifier.Operation.ADD_NUMBER)
                );
                lore.add("more max health");
                break;
        }

        craft_meta.setLore(lore);

        craft_result.setItemMeta(craft_meta);
        return craft_result;
    }

    private long lastCheckTime = System.currentTimeMillis();

    boolean amnesia = false;

    public Material get_crafting_scramble(Material craft_attempted) {
        if (this.random_crafting_results.containsKey(craft_attempted)) {
            Material random_mat = this.random_crafting_results.get(craft_attempted);
            getServer().getLogger().info("item already exists");
            return random_mat;
        } else {
            Material new_random_result = this.get_random_mat();
            if (!amnesia) {
                this.random_crafting_results.put(craft_attempted, new_random_result);
            }
            getServer().getLogger().info("added new generated item");
            return new_random_result;
        }
    }

    public void amnesiaEvent() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastCheckTime >= (1000 * 60) * 10) { // 10 mins has passed
            lastCheckTime = currentTime; // Reset the timer
            // clear all saved recipes
            this.random_crafting_results.clear();
            // tell every player that you reset the recipes
            for ( Player player : getServer().getOnlinePlayers() ) {
                player.sendMessage("ALL RECIPES HAVE BEEN CLEARED");
            }
        }
    }

    // just some events that happen often
    @EventHandler
    public void amnesiaTrigger1(PlayerMoveEvent e) {
        this.amnesiaEvent();
    }
    @EventHandler
    public void amnesiaTrigger2(PlayerJumpEvent e) {
        this.amnesiaEvent();
    }
    @EventHandler
    public void amnesiaTrigger3(PlayerInteractEvent e) {
        this.amnesiaEvent();
    }

    @EventHandler
    public void crafterCraftEvent(CrafterCraftEvent craft_attempt) {
        if (!craft_attempt.getResult().isEmpty()) {
            craft_attempt.setResult(scramble_tools(get_crafting_scramble(craft_attempt.getResult().getType())));
        }
    }

    @EventHandler
    public void playerCraftInteract(PrepareItemCraftEvent craft_attempt) {
        boolean is_valid_recipe = false;
        try {
            is_valid_recipe = !craft_attempt.getInventory().getResult().isEmpty();
        } catch (NullPointerException e) {
            getServer().getLogger().info("empty crafting recipe detected");
        }

        if (is_valid_recipe) {
            Material craft_attempted = craft_attempt.getInventory().getResult().getType();
            getServer().getLogger().info("valid crafting recipe detected");
            Material mat = get_crafting_scramble(craft_attempted);
            craft_attempt.getInventory().setResult(scramble_tools(mat));
        }
    }

}
