package rimon.statsByRimon;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class StatsGUI {

    public static void openStatsGUI(Player player, StatsByRimon plugin) {
        // GUI title
        String title = ChatColor.translateAlternateColorCodes('&',
                PlaceholderAPI.setPlaceholders(player, plugin.getConfig().getString("gui-title", "&6Stats")));

        // GUI size (defaults to 27 if not set)
        int size = plugin.getConfig().getInt("gui-size", 27);
        Inventory gui = Bukkit.createInventory(null, size, title);

        // Items from config
        ConfigurationSection items = plugin.getConfig().getConfigurationSection("items");





        if (items != null) {
            for (String key : items.getKeys(false)) {
                ConfigurationSection section = items.getConfigurationSection(key);
                if (section == null) continue;

                // Material
                Material mat = Material.matchMaterial(section.getString("material", "STONE").toUpperCase());
                if (mat == null) continue;

                ItemStack item = new ItemStack(mat);
                ItemMeta meta = item.getItemMeta();

                // Skull support
                if (mat == Material.PLAYER_HEAD && section.contains("skull")) {
                    SkullMeta skullMeta = (SkullMeta) meta;
                    String skullOwner = PlaceholderAPI.setPlaceholders(player, section.getString("skull"));
                    skullMeta.setOwner(skullOwner); // deprecated but works on latest Spigot
                    meta = skullMeta;
                }

                // Display name
                if (section.contains("name")) {
                    String name = PlaceholderAPI.setPlaceholders(player, section.getString("name"));
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                }

                // Lore
                if (section.contains("lore")) {
                    List<String> lore = new ArrayList<>();
                    for (String line : section.getStringList("lore")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&',
                                PlaceholderAPI.setPlaceholders(player, line)));
                    }
                    meta.setLore(lore);
                }

                // Glow
                if (section.getBoolean("glow", false)) {
                    meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                item.setItemMeta(meta);

                // Slot placement
                List<Integer> slots = new ArrayList<>();
                if (section.isList("slot")) {
                    // Handles the standard YAML list format: slot: [1, 2, 3]
                    slots.addAll(section.getIntegerList("slot"));
                } else if (section.isString("slot")) {
                    // Handles the comma-separated string format: slot: "1,2,3"
                    String[] slotStrings = section.getString("slot").split(",");
                    for (String s : slotStrings) {
                        try {
                            slots.add(Integer.parseInt(s.trim()));
                        } catch (NumberFormatException e) {
                            // Log an error or warning that a slot value was not a valid number
                            System.out.println("Warning: Invalid slot number '" + s + "' in config section '" + section.getName() + "'");
                        }
                    }
                } else if (section.isInt("slot")) {
                    // Handles the original single integer format for backward compatibility
                    slots.add(section.getInt("slot"));
                }


                for (int slot : slots) {
                    if (slot >= 0 && slot < size) {
                        gui.setItem(slot, item);
                    }
                }
            }
            // Open GUI
            player.openInventory(gui);
        }
    }
}
