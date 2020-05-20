package university.quaranteen.gradcraft.ceremony.commands;

import com.google.protobuf.Message;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import university.quaranteen.gradcraft.GradCraftPlugin;
import com.bergerkiller.bukkit.common.MessageBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListCommand implements CommandExecutor {
    public ListCommand(GradCraftPlugin plugin) {
        this.db = plugin.db;
    }

    private HikariDataSource db;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
