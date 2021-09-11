package dev.kotw.lobbyplugin;

import dev.kotw.lobbyplugin.commands.LobbySetupCommand;
import dev.kotw.lobbyplugin.listeners.InventoryClickListener;
import dev.kotw.lobbyplugin.listeners.PlayerInteractListener;
import dev.kotw.lobbyplugin.listeners.PlayerItemListener;
import dev.kotw.lobbyplugin.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bLobby§7] §r";
    public static String syntax;
    public static FileConfiguration config;

    public static Inventory navigator;
    public static HashMap<Integer, Location> navigatorLocations = new HashMap<>();

    public static Location spawnPoint;

    @Override
    public void onEnable() {
        // Load config
        File configFile = new File(getDataFolder(), "config.yml");
        config = getConfig();

        if(!configFile.exists()) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new File(getClass().getResource("/config.yml").getFile())));
            config.options().copyDefaults(true);
            saveDefaultConfig();
            reloadConfig();
            config = YamlConfiguration.loadConfiguration(configFile);
        }
        
        // Spawnpoint
        if(config.getString("spawn")!=null) spawnPoint = new Location(Bukkit.getWorld(UUID.fromString(config.getString("spawn.pos.world"))), config.getDouble("spawn.pos.x"), config.getDouble("spawn.pos.y"), config.getDouble("spawn.pos.z"), (float)config.getDouble("spawn.pos.yaw"), (float)config.getDouble("spawn.pos.pitch"));

        // Navigator loading
        navigator = Bukkit.createInventory(null, 5*9, config.getString("navigatorTitle"));
        ItemStack gray = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)7);
        ItemMeta im = gray.getItemMeta();
        im.setDisplayName(" ");
        gray.setItemMeta(im);
        for(int i = 0; i < 5*9; i++) navigator.setItem(i, gray);
        ConfigurationSection section = config.getConfigurationSection("navigator");
        if(section!=null) for(String it : section.getKeys(false)) {
            navigatorLocations.put(section.getInt(it + ".index"), new Location(Bukkit.getWorld(UUID.fromString(section.getString(it + ".pos.world"))), section.getDouble(it + ".pos.x"), section.getDouble(it + ".pos.y"), section.getDouble(it + ".pos.z"), (float)section.getDouble(it + ".pos.yaw"), (float)section.getDouble(it + ".pos.pitch")));
            navigator.setItem(section.getInt(it + ".index"), section.getItemStack(it + ".item"));
        }

        syntax = config.getString("syntax");

        // Commands
        getCommand("lobbysetup").setPermissionMessage(Main.PREFIX + config.getString("noPermission")).setPermission(config.getString("permissions.setup"));
        getCommand("lobbysetup").setExecutor(new LobbySetupCommand());
        getCommand("lobbysetup").setTabCompleter(new LobbySetupCommand());

        // Listeners
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerInteractListener(), this);
        manager.registerEvents(new InventoryClickListener(), this);
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new PlayerItemListener(), this);

        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§2loaded successfully.");
    }

    @Override
    public void onDisable() {
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§2successfully disabled.");
    }
}
