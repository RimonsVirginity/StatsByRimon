package rimon.statsByRimon;


import rimon.statsByRimon.StatsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class openCommand implements CommandExecutor {
    private final StatsByRimon plugin;

    public openCommand(StatsByRimon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        StatsGUI.openStatsGUI(player, plugin);
        return true;
    }
}
