package university.quaranteen.gradcraft.ceremony;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.destroystokyo.paper.Title;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.sql.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActiveCeremony {
    public ActiveCeremony(int id, World world, GradCraftPlugin plugin) {
        this.id = id;
        this.db = plugin.db;
        this.stageController = new StageController(world, plugin);
    }

    private int id;
    private World world;
    private Player controller;
    private HikariDataSource db;
    private Graduate currentGraduate;
    private final StageController stageController;

    public int getId() {
        return id;
    }

    public World getWorld() {
        return stageController.getWorld();
    }

    public Player getController() {
        return controller;
    }

    public StageController getStageController() { return stageController; }

    public Graduate getCurrentGraduate() {
        return currentGraduate;
    }

    public void setController(Player player) {
        this.controller = player;
    }

    public Graduate nextGraduate() {
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT g.id, g.name, pronunciation, degreeLevel, honors,  major, seniorQuote, uuid, u.name, graduated, timeslot " +
                    "from graduates g join universities u on g.university = u.id " +
                    "where not graduated and ceremony = ? " +
                    "order by timeslot asc "+
                    "limit 1;");
            stmt.setInt(1, this.id);
            res = stmt.executeQuery();
            Graduate g = null;
            if (res.next()) {
                g = new Graduate(
                        res.getInt(1),
                        this,
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getString(6),
                        res.getString(7),
                        res.getString(8),
                        res.getString(9),
                        res.getBoolean(10),
                        res.getTimestamp(11)
                );
            }
            res.close();
            c.close();

            this.currentGraduate = g;
            this.stageController.nextGraduate(g);

            return g;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void signalGraduated(Graduate graduate) {
        assert(graduate != null);
        Connection c;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("UPDATE graduates SET graduated = 1 WHERE id = ?;");
            stmt.setInt(1, graduate.getId());
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
