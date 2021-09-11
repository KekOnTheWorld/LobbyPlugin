package dev.kotw.lobbyplugin.commands;

import dev.kotw.lobbyplugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbySetupCommand implements TabExecutor {
    public static final String SYNTAX = "/lobbysetup <add | remove | position | help> [localizedname] [index]";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length > 0) {
                switch (args[0]) {
                    case "add":
                        if(args.length == 3) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            if (item != null) {
                                try {
                                    Location location = player.getLocation();
                                    int index = Integer.parseInt(args[2]);
                                    if(index < 0 || index > 54) throw new Exception("not in range");
                                    Main.config.set("navigator." + args[1] + ".item", item);
                                    Main.config.set("navigator." + args[1] + ".index", index);
                                    Main.config.set("navigator." + args[1] + ".pos.x", location.getX());
                                    Main.config.set("navigator." + args[1] + ".pos.y", location.getY());
                                    Main.config.set("navigator." + args[1] + ".pos.z", location.getZ());
                                    Main.config.set("navigator." + args[1] + ".pos.pitch", location.getPitch());
                                    Main.config.set("navigator." + args[1] + ".pos.yaw", location.getYaw());
                                    Main.config.set("navigator." + args[1] + ".pos.world", location.getWorld().getUID().toString());
                                    player.sendMessage(Main.PREFIX + "§aItem added.");
                                } catch (Exception e) {
                                    player.sendMessage(Main.PREFIX + "§c<index> must be an int between 0 and 54.");
                                }
                            } else player.sendMessage(Main.PREFIX + "§cPlease put an item in your main hand and try again!");
                        } else player.sendMessage(Main.PREFIX + Main.syntax.replace("!!0", SYNTAX));
                        break;

                    case "remove":
                        Main.config.set("navigator." + args[1], null);
                        player.sendMessage(Main.PREFIX + "§aItem removed.");
                        break;

                    case "position":
                        if(args.length == 2) {
                            Location location = player.getLocation();
                            Main.config.set("navigator." + args[1] + ".pos.x", location.getX());
                            Main.config.set("navigator." + args[1] + ".pos.y", location.getY());
                            Main.config.set("navigator." + args[1] + ".pos.z", location.getZ());
                            Main.config.set("navigator." + args[1] + ".pos.pitch", location.getPitch());
                            Main.config.set("navigator." + args[1] + ".pos.yaw", location.getYaw());
                            Main.config.set("navigator." + args[1] + ".pos.world", location.getWorld().getUID().toString());
                            player.sendMessage(Main.PREFIX + "§aItem position changed.");
                        }
                        break;

                    case "spawnpoint":
                        Location location = player.getLocation();
                        Main.config.set("spawn.pos.x", location.getX());
                        Main.config.set("spawn.pos.y", location.getY());
                        Main.config.set("spawn.pos.z", location.getZ());
                        Main.config.set("spawn.pos.pitch", location.getPitch());
                        Main.config.set("spawn.pos.yaw", location.getYaw());
                        Main.config.set("spawn.pos.world", location.getWorld().getUID().toString());
                        player.sendMessage(Main.PREFIX + "§aSpawnpoint set.");
                        break;

                    case "help":
                        player.sendMessage(Main.PREFIX + "§6HELP:");
                        player.sendMessage(Main.PREFIX + "§7/lobbysetup add <localizedname> <index> §6adds the item in your main hand to the inventory");
                        player.sendMessage(Main.PREFIX + "§7/lobbysetup remove <localizedname> §6Removes the item");
                        player.sendMessage(Main.PREFIX + "§7/lobbysetup position <localizedname> §6sets the warp position of the item");
                        player.sendMessage(Main.PREFIX + "§7/rl §6after you changed the items");
                        player.sendMessage(Main.PREFIX + "§6The navigator is triggered by a compas item.");
                        break;

                    default:
                        player.sendMessage(Main.PREFIX + Main.syntax.replace("!!0", SYNTAX));
                }
            } else {
                ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', im.getDisplayName()));
                player.getInventory().getItemInMainHand().setItemMeta(im);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1) {
            for(String st : new String[] {"add", "remove", "position", "help", "spawnpoint"}) {
                if(st.startsWith(args[0].toLowerCase())) result.add(st);
            }
        } else if(args.length == 2 && (args[0].equalsIgnoreCase("position")||args[0].equalsIgnoreCase("remove"))) {
            if(Main.config.getConfigurationSection("navigator")!=null) for(String st : Main.config.getConfigurationSection("navigator").getKeys(false)) if(st.toLowerCase().startsWith(args[1].toLowerCase())) result.add(st);
        }
        return result;
    }
}
