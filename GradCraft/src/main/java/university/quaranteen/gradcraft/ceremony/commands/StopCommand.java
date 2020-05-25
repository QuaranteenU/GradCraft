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
import university.quaranteen.gradcraft.GradCraftPlugin;

import javax.annotation.Nonnull;

public class StopCommand implements CommandExecutor {
    public StopCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private final GradCraftPlugin plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
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
