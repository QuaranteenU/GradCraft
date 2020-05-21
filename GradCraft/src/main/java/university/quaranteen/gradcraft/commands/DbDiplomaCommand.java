package university.quaranteen.gradcraft.commands;

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
    public static final String GET_USER_NAME_AND_MAJOR_QUERY = "SELECT name, degreeLevel, major " +
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
            try {
                Connection c = plugin.db.getConnection();
                PreparedStatement stmt = c.prepareStatement(GET_USER_NAME_AND_MAJOR_QUERY);
                stmt.setString(1, player.getUniqueId().toString());
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    player.sendMessage("Congratulations, " + player.getName() + "! Here's your diploma!");
                    String name = res.getString(1);
                    String degreeLevel = res.getString(2);
                    String major = res.getString(3);
                    player.getInventory().addItem(new Diploma(player, name, major, degreeLevel).createItem());
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
        }
        return true;
    }
}
