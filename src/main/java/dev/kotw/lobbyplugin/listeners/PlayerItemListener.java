package dev.kotw.lobbyplugin.listeners;

import dev.kotw.lobbyplugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemListener implements Listener {
    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if(!e.getPlayer().hasPermission(Main.config.getString("permissions.drop"))) e.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(!player.hasPermission(Main.config.getString("permissions.pickup"))) e.setCancelled(true);
        }
    }
}
