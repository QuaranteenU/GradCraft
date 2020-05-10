package university.quaranteen.gradcraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.sql.*;

public class DbDiplomaCommand implements CommandExecutor {
    //language=SQL
    private static final String GET_USER_NAME_AND_MAJOR_QUERY = "SELECT name, degreeLevel, major " +
            "FROM graduates " +
            "WHERE uuid=?;";
    private final GradCraftPlugin plugin;

    public DbDiplomaCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                this.plugin.getLogger().info("Congratulations, " + player.getDisplayName() + " \"" + player.getUniqueId().toString() + "\"! Here's your diploma!");
                Connection c = plugin.db.getConnection();
                PreparedStatement stmt = c.prepareStatement(GET_USER_NAME_AND_MAJOR_QUERY);
                stmt.setString(1, player.getUniqueId().toString());
                ResultSet res = stmt.executeQuery();
                player.sendMessage("Congratulations, " + player.getDisplayName() + " \"" + player.getUniqueId().toString() + "\"! Here's your diploma!");
                if (res.next()) {
                    String name = res.getString(1);
                    String degreeLevel = res.getString(2);
                    String major = res.getString(3);
                    player.getInventory().addItem(new Diploma(player, name, major, degreeLevel).createItem());
                }
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
