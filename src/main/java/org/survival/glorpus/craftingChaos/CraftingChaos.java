package org.survival.glorpus.craftingChaos;

import io.papermc.paper.datacomponent.DataComponentType;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.logging.Level;

public final class CraftingChaos extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CraftingListener(this), this);
        getServer().getPluginManager().registerEvents(new AlternateCrafting(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

// old code
//public final class CraftingChaos extends JavaPlugin {
//
//    @Override
//    public void onEnable() {
//
//        Vector<Recipe> recipes = new Vector<>();
//        Vector<ItemStack> crafting_outputs = new Vector<>();
//
//        Iterator<Recipe> recipe_iterator = Bukkit.recipeIterator();
//        while (recipe_iterator.hasNext()) {
//            Recipe current_recipe = recipe_iterator.next();
//            crafting_outputs.add(current_recipe.getResult());
//        }
//
//        Bukkit.getLogger().log(Level.INFO, "adding recipes...");
//
//        // while there are still values left in the list, add the random recipe and its outcome, then remove it
//        Random random = new Random();
//        while (!recipes.isEmpty()) {
//            int randRecipeIndex = random.nextInt(recipes.size());
//            int itemIndex = random.nextInt(crafting_outputs.size());
//
//            Recipe current_recipe = recipes.get(randRecipeIndex);
//            if (current_recipe instanceof ShapelessRecipe) {
//                ShapelessRecipe shapeless_recipe = (ShapelessRecipe) current_recipe;
//
//            }
//            if (current_recipe instanceof ShapedRecipe) {
//                ShapedRecipe shaped_recipe = (ShapedRecipe) current_recipe;
//            }
//
//            getServer().addRecipe(current_recipe);
//
//            crafting_outputs.remove(itemIndex);
//            recipes.remove(randRecipeIndex);
//        }
//
//    }
//
//    @Override
//    public void onDisable() {
//        // Plugin shutdown logic
//    }
//}
