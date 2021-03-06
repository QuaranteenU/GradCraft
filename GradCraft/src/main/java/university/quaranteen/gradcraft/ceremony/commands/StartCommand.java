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
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.ceremony.ActiveCeremony;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class StartCommand implements CommandExecutor {
    public StartCommand(GradCraftPlugin plugin) {
        this.db = plugin.db;
        this.gradWorld = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
        this.plugin = plugin;
    }

    private final HikariDataSource db;
    private final World gradWorld;
    private final GradCraftPlugin plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't run this from console!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length < 1) {
            return false;
        }

        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT id, name, startTime FROM ceremonies WHERE id=?;");
            stmt.setInt(1, Integer.parseInt(args[0]));
            res = stmt.executeQuery();
            if (!res.next()) {
                sender.sendMessage(new MessageBuilder()
                    .dark_red("Ceremony not found! Do ")
                    .white("/cerlist ")
                    .dark_red("for a list.")
                    .toString());
                res.close();
                c.close();
                return true;
            }

            ActiveCeremony ceremony = new ActiveCeremony(res.getInt(1), gradWorld, plugin);
            ceremony.setController(p);
            plugin.ceremony = ceremony;

            // announce to server
            plugin.getServer().broadcastMessage(new MessageBuilder()
                    .append(ChatColor.BOLD, "Graduation is starting soon!")
                    .newLine()
                    .append(ChatColor.RESET, "")
                    .white("If you're scheduled to graduate during the ")
                    .aqua(res.getString(2))
                    .white(" ceremony, make your way to the graduation stage now!")
                    .toString()
            );
            res.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
