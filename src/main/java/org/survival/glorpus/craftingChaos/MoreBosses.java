package org.survival.glorpus.craftingChaos;

import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Vector;

public class MoreBosses {

    // add some fun bosses

    public void spawn_dirt_gremlin(Plugin plugin, Player player) {

        Boss dirt_gremlin = new Boss(plugin, player, EntityType.ZOMBIE);
        Entity gremlin_entity = dirt_gremlin.get_entity();
        gremlin_entity.teleport(player.getLocation().add(0.0, 1.0, 0.0));
        gremlin_entity.setVelocity(new org.bukkit.util.Vector(4.0/10, 0.0, 7.0/10));

        CustomItems custom_items = new CustomItems(plugin);

        ((LivingEntity) gremlin_entity).getEquipment().setHelmet(custom_items.dirtGremlinHelmet());
        ((Zombie) gremlin_entity).setBaby();

        // set the loot table
        Vector<ItemStack> loot_table = new Vector<>();
        loot_table.add(custom_items.dynamiteGlove());
        dirt_gremlin.setLootTable(loot_table);

        // set the attack patterns
        AttackPatterns attackPatterns = new AttackPatterns(dirt_gremlin.get_entity().getUniqueId(), player);

        AttackPattern bomb = new AttackPattern(3);
        bomb.add_move(MoveType.LEAP);
        bomb.add_move(MoveType.LEAP_BOMB);
        bomb.add_move(MoveType.LEAP);
        attackPatterns.add_pattern(bomb);

        AttackPattern run = new AttackPattern(1);
        run.add_move(MoveType.LEAP);
        run.add_move(MoveType.LEAP);
        attackPatterns.add_pattern(run);

        AttackPattern heal = new AttackPattern(2);
        heal.add_move(MoveType.THROW_INSTANT_DAMAGE);
        heal.add_move(MoveType.LEAP);
        heal.add_move(MoveType.THROW_INSTANT_DAMAGE);
        attackPatterns.add_pattern(heal);

        AttackPattern pause = new AttackPattern(0.1);
        pause.add_move(MoveType.WAIT);
        attackPatterns.add_pattern(pause);

        dirt_gremlin.setAttackPatterns(attackPatterns);
        dirt_gremlin.registerBoss();
    }

}
