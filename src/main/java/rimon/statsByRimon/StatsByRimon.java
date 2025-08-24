package rimon.statsByRimon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class StatsByRimon extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("Stats").setExecutor(new openCommand(this));
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        getLogger().info("StatsByRimon enabled!");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
