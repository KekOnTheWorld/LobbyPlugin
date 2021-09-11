package dev.kotw.lobbyplugin.listeners;

import dev.kotw.lobbyplugin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == Main.navigator) {
            e.setCancelled(true);
            if(Main.navigatorLocations.containsKey(e.getSlot())) {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().teleport(Main.navigatorLocations.get(e.getSlot()));
            }
        }
    }
}
