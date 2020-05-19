package university.quaranteen.gradcraft.ceremony.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.ceremony.Graduate;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.Temporal;

public class StatusCommand implements CommandExecutor {
    public StatusCommand(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    private GradCraftPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.ceremony == null) {
            sender.sendMessage(new MessageBuilder()
                    .red("There is no active ceremony.")
                    .toString()
            );
            return true;
        }

        Connection c;
        ResultSet res;
        long totalGrads = 0, gradsLeft = 0;
        Timestamp startTime, finishTime;
        Duration delay;
        try {
            c = plugin.db.getConnection();

            PreparedStatement stmt = c.prepareStatement("select count(*), min(timeslot), max(timeslot) from graduates where ceremony = ?");
            stmt.setInt(1, plugin.ceremony.getId());
            res = stmt.executeQuery();
            if (res.next()) {
                totalGrads = res.getLong(1);
                startTime = res.getTimestamp(2);
                finishTime = res.getTimestamp(3);
            } else {
                startTime = Timestamp.from(Instant.now());
                finishTime = Timestamp.from(Instant.now());
            }
            res.close();

            stmt = c.prepareStatement("select count(*) from graduates where not graduated and ceremony = ?");
            stmt.setInt(1, plugin.ceremony.getId());
            res = stmt.executeQuery();
            if (res.next()) {
                gradsLeft = res.getLong(1);
            }
            res.close();

            /*Graduate g = plugin.ceremony.getCurrentGraduate();
            if (g == null && startTime != null) {
                delay = Duration.between((Temporal) startTime, Instant.now());
            } else if (g != null) {
                assert g != null;
                delay = g.getShowDelay();
            }*/

            sender.sendMessage(new MessageBuilder()
                    .green("Ceremony #", plugin.ceremony.getId(), " in progress")
                    .newLine()
                    .white("Controller: ", plugin.ceremony.getShowRunner().getName())
                    .newLine()
                    .green(gradsLeft, "/", totalGrads, " remaining")
                    .newLine()
                    .green("Estimated finish time: ", finishTime)
                    .newLine()
                    //.green("Current delay: ", delay.toString())
                    //.newLine()
                    .green("Current UTC time: ", Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime())
                    .toString()
            );

            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
