package university.quaranteen.gradcraft.ceremony.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class JoinQueueCommand implements CommandExecutor {
    public JoinQueueCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private GradCraftPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
