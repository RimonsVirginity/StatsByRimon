package rimon.statsByRimon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final StatsByRimon plugin;

    public GUIListener(StatsByRimon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof StatsHolder) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player)) return;

        }
    }
}
