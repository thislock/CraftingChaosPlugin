package org.survival.glorpus.craftingChaos;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Weapons implements Listener {

    CustomItems custom_items;
    Plugin plugin;

    public Weapons(Plugin plugin) {
        custom_items = new CustomItems(plugin);
        this.plugin = plugin;
    }

}
