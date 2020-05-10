package university.quaranteen.gradcraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import university.quaranteen.gradcraft.diploma.Diploma;

import javax.annotation.Nonnull;

public class DiplomaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Diploma d = new Diploma(p, p.getDisplayName(), "Underwater Basket Weaving", "Bachelor's");
            ItemStack item = d.createItem();
            p.getInventory().addItem(item);
            sender.sendMessage("Diploma given to: " + p.getDisplayName());
            return true;
        } else {
            sender.sendMessage("You may not create a Diploma card!");
            return false;
        }
    }
}
