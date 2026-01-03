package rimon.statsByRimon;


import rimon.statsByRimon.StatsGUI;
import org.bukkit.Bukkit;
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
        // 1. Ensure the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player viewer = (Player) sender;
        Player target;

        if (args.length == 0) {
            target = viewer;
        } else {
            target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                viewer.sendMessage("§cPlayer '" + args[0] + "' is not online!");
                return true;
            }
        }

        StatsGUI.openStatsGUI(viewer, target, plugin);

        return true;
    }
}
