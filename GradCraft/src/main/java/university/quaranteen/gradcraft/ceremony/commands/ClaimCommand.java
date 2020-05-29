/*
    This file is part of GradCraft, by the Quaranteen University team.
    https://quaranteen.university

    GradCraft is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GradCraft is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GradCraft.  If not, see <https://www.gnu.org/licenses/>.
 */

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
