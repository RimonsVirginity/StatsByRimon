package rimon.statsByRimon;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIListener implements Listener {

    private final StatsByRimon plugin;

    // Constructor to get the main plugin instance
    public GUIListener(StatsByRimon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String configTitle = plugin.getConfig().getString("gui-title", "&6Stats");
        String expectedTitle = ChatColor.translateAlternateColorCodes('&',
                PlaceholderAPI.setPlaceholders(player, configTitle));

        String clickedInventoryTitle = event.getView().getTitle();


        if (clickedInventoryTitle.equals(expectedTitle)) {
            Inventory clickedInventory = event.getClickedInventory();
            Inventory topInventory = event.getView().getTopInventory();

            if (clickedInventory != null && clickedInventory.equals(topInventory)) {
                event.setCancelled(true);
            }
        }
    }
}
