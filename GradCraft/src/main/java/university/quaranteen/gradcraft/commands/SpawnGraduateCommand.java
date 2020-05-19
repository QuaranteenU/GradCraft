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

public class SpawnGraduateCommand implements CommandExecutor {
    private GradCraftPlugin plugin;

    public SpawnGraduateCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
            Location tpInLocation = new Location(
                    world,
                    plugin.config.getDouble("gradTpPoint.x"),
                    plugin.config.getDouble("gradTpPoint.y"),
                    plugin.config.getDouble("gradTpPoint.z"));

            GraduateNPC grad = new GraduateNPC(sender, tpInLocation);
            if (plugin.currentGraduateNPC != null) {
                plugin.currentGraduateNPC.destroy();
            }
            plugin.currentGraduateNPC = grad;
            sender.sendMessage("created graduate npc");
            return true;
        } else {
            sender.sendMessage("you're not a player bucko");
            return false;
        }
    }
}