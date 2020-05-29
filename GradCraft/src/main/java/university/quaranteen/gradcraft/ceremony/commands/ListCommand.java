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

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import university.quaranteen.gradcraft.GradCraftPlugin;
import com.bergerkiller.bukkit.common.MessageBuilder;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListCommand implements CommandExecutor {
    public ListCommand(GradCraftPlugin plugin) {
        this.db = plugin.db;
    }

    private final HikariDataSource db;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT c.id, c.name, c.startTime, COUNT(g.id) as graduates\n" +
                    "FROM ceremonies c LEFT OUTER JOIN graduates g ON c.id = g.ceremony\n" +
                    "GROUP BY c.id, c.name, c.startTime " +
                    "ORDER BY c.id;");
            res = stmt.executeQuery();
            while (res.next()) {
                sender.sendMessage(new MessageBuilder()
                        .gray("ID #")
                        .append(ChatColor.BOLD, res.getInt(1))
                        .append(ChatColor.RESET, " | ")
                        .green(res.getTimestamp(3))
                        .append(ChatColor.RESET, " | ")
                        .red(res.getInt(4))
                        .append(ChatColor.RESET, " graduates | ")
                        .white(res.getString(2))
                        .toString()
                );
            }
            res.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
