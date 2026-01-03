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

    public static void openStatsGUI(Player viewer, Player target, StatsByRimon plugin) {

        String title = ChatColor.translateAlternateColorCodes('&',
                PlaceholderAPI.setPlaceholders(target, plugin.getConfig().getString("gui-title", "&6Stats")));

        int size = plugin.getConfig().getInt("gui-size", 27);
        Inventory gui = Bukkit.createInventory(new StatsHolder(), size, title);

        ConfigurationSection items = plugin.getConfig().getConfigurationSection("items");

        if (items != null) {
            for (String key : items.getKeys(false)) {
                ConfigurationSection section = items.getConfigurationSection(key);
                if (section == null) continue;

                Material mat = Material.matchMaterial(section.getString("material", "STONE").toUpperCase());
                if (mat == null) continue;

                ItemStack item = new ItemStack(mat);
                ItemMeta meta = item.getItemMeta();

                if (mat == Material.PLAYER_HEAD && section.contains("skull")) {
                    SkullMeta skullMeta = (SkullMeta) meta;
                    String skullOwner = PlaceholderAPI.setPlaceholders(target, section.getString("skull"));
                    skullMeta.setOwner(skullOwner);
                    meta = skullMeta;
                }

                if (section.contains("name")) {
                    String name = PlaceholderAPI.setPlaceholders(target, section.getString("name"));
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                }

                if (section.contains("lore")) {
                    List<String> lore = new ArrayList<>();
                    for (String line : section.getStringList("lore")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&',
                                PlaceholderAPI.setPlaceholders(target, line)));
                    }
                    meta.setLore(lore);
                }

                if (section.getBoolean("glow", false)) {
                    meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                item.setItemMeta(meta);

                List<Integer> slots = new ArrayList<>();
                if (section.isList("slot")) {
                    slots.addAll(section.getIntegerList("slot"));
                } else if (section.isString("slot")) {
                    String[] slotStrings = section.getString("slot").split(",");
                    for (String s : slotStrings) {
                        try {
                            slots.add(Integer.parseInt(s.trim()));
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Invalid slot number '" + s + "' in config section '" + section.getName() + "'");
                        }
                    }
                } else if (section.isInt("slot")) {
                    slots.add(section.getInt("slot"));
                }

                for (int slot : slots) {
                    if (slot >= 0 && slot < size) {
                        gui.setItem(slot, item);
                    }
                }
            }
            viewer.openInventory(gui);
        }
    }
}
