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
import university.quaranteen.gradcraft.ceremony.Graduate;
import university.quaranteen.gradcraft.ceremony.StageController;

import javax.annotation.Nonnull;

public class NextCommand implements CommandExecutor {
    public NextCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private final GradCraftPlugin plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (plugin.ceremony == null) {
            sender.sendMessage(new MessageBuilder()
                    .red("There isn't an active ceremony! Use /cerlist and /cerstart to begin")
                    .toString()
            );
            return true;
        }
        if (plugin.ceremony.getShowRunner() != sender) {
            sender.sendMessage(new MessageBuilder()
                    .red("You're not the show runner!")
                    .toString()
            );
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(new MessageBuilder().red("This command doesn't work from the console.").toString());
            return true;
        }

        // todo: refactor this to allow automatic advancement
        StageController controller = plugin.ceremony.getStageController();
        plugin.ceremony.signalGraduated(plugin.ceremony.getCurrentGraduate());
        controller.forceOutGraduate();
        Graduate g = plugin.ceremony.getNextGraduate();
        if (g == null) {
            sender.sendMessage(new MessageBuilder().red("This ceremony is complete!").toString());
            return true;
        }
        plugin.ceremony.setCurrentGraduate(plugin.ceremony.getNextGraduate());
        controller.teleportInGraduate();
        controller.notifyShowRunner(plugin.ceremony.getShowRunner());
        controller.notifySpectators();

        return true;
    }
}
