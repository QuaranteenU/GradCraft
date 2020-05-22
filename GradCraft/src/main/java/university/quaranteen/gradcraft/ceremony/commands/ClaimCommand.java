package university.quaranteen.gradcraft.ceremony.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;

import javax.annotation.Nonnull;

public class ClaimCommand implements CommandExecutor {
    public ClaimCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private final GradCraftPlugin plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new MessageBuilder()
                    .red("Only players can run this command.")
                    .toString()
            );
            return true;
        }
        if (plugin.ceremony == null) {
            sender.sendMessage(new MessageBuilder()
                    .red("A ceremony hasn't been started yet! Use /cerstart to begin.")
                    .toString()
            );
            return true;
        }
        if (plugin.ceremony.getShowRunner() == sender) {
            sender.sendMessage(new MessageBuilder()
                    .red("You're already the show runner!")
                    .toString()
            );
            return true;
        }

        plugin.ceremony.setController((Player) sender);
        return true;
    }
}
