package university.quaranteen.gradcraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.citizens.GraduateNPC;

import javax.annotation.Nonnull;

public class NPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            GraduateNPC grad = new GraduateNPC();
            grad.TestMethod();
            sender.sendMessage("did the thang b");
            return true;
        } else {
            sender.sendMessage("you're not a player bucko");
            return false;
        }
    }
}