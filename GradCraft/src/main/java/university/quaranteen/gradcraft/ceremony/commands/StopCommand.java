package university.quaranteen.gradcraft.ceremony.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class StopCommand implements CommandExecutor {
    public StopCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private GradCraftPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender != plugin.ceremony.getShowRunner()) {
            sender.sendMessage(new MessageBuilder()
                    .red("You're not the show runner!")
                    .toString()
            );
            return true;
        }

        if (plugin.ceremony == null) {
            sender.sendMessage(new MessageBuilder()
                    .red("There isn't a ceremony running.")
                    .toString()
            );
            return true;
        }



        plugin.ceremony.getStageController().stopCeremony(plugin.ceremony.getId(), plugin.ceremony.getShowRunner());
        plugin.ceremony = null;

        return true;
    }
}
