package dev.kotw.lobbyplugin.listeners;

import dev.kotw.lobbyplugin.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Inventory inventory = e.getPlayer().getInventory();
        inventory.clear();
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta im = compass.getItemMeta();
        im.setDisplayName(Main.config.getString("navigatorTitle"));
        compass.setItemMeta(im);
        if(Main.spawnPoint!=null) e.getPlayer().teleport(Main.spawnPoint);
        inventory.setItem(4, compass);
    }
}
