package dev.kotw.lobbyplugin.listeners;

import dev.kotw.lobbyplugin.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                e.setCancelled(true);
                e.getPlayer().openInventory(Main.navigator);
            }
        }
        if(e.getAction()!=Action.RIGHT_CLICK_BLOCK||e.getClickedBlock().getType()!=Material.CHEST) {
            if(!e.getPlayer().hasPermission(Main.config.getString("permissions.interact"))) e.setCancelled(true);
        }
    }
}
