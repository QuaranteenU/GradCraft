package university.quaranteen.gradcraft.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.citizens.GraduateNPC;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DestroyGraduateCommand implements CommandExecutor {
    private GradCraftPlugin plugin;

    public DestroyGraduateCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            plugin.currentGraduateNPC.destroy();
            plugin.currentGraduateNPC = null;
            sender.sendMessage("destroyed graduate npc");
            return true;
        } else {
            sender.sendMessage("you're not a player bucko");
            return false;
        }
    }
}