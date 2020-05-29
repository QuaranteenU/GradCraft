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

package university.quaranteen.gradcraft.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.sql.*;

import javax.annotation.Nonnull;

public class DbDiplomaCommand implements CommandExecutor {
    //language=SQL
    public static final String GET_USER_DATA_QUERY = "SELECT name, degreeLevel, major, university, isHighSchool " +
            "FROM graduates " +
            "WHERE uuid=?;";
    private final GradCraftPlugin plugin;

    public DbDiplomaCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.getInventory().contains(Material.FILLED_MAP)) {

                try {
                    Connection c = plugin.db.getConnection();
                    PreparedStatement stmt = c.prepareStatement(GET_USER_DATA_QUERY);
                    stmt.setString(1, player.getUniqueId().toString());
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        player.sendMessage("Congratulations, " + player.getName() + "! Here's your diploma!");
                        String name = res.getString(1);
                        String degreeLevel = res.getString(2);
                        String major = res.getString(3);
                        String school = res.getString(4);
                        boolean isHighschool = res.getBoolean(5);
                        if (isHighschool)
                            player.getInventory().addItem(new Diploma(player, name, school, isHighschool).createItem());
                        else
                            player.getInventory().addItem(new Diploma(player, name, major, degreeLevel, isHighschool).createItem());
                    } else {
                        player.sendMessage("You're not a graduate! :(");
                    }
                    res.close();
                    c.close();
                    return true;
                } catch (SQLException ex) {
                    this.plugin.getLogger().severe(player.getDisplayName() + " \"" + player.getUniqueId().toString() + "\" - diploma failed");
                    ex.printStackTrace();
                    return true;
                }
            } else {
                player.sendMessage("You've already received your diploma!");
            }
        }
        return true;
    }
}
