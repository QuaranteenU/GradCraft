package university.quaranteen.gradcraft.ceremony;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActiveCeremony {
    public ActiveCeremony(int id, World world, HikariDataSource db) {
        this.id = id;
        this.world = world;
        this.db = db;
        this.onlineGraduateQueue = new ConcurrentLinkedQueue<>();
    }

    private int id;
    private World world;
    private Player controller;
    private Queue<Graduate> onlineGraduateQueue;
    private HikariDataSource db;

    public int getId() {
        return id;
    }

    public World getWorld() {
        return world;
    }

    public Queue<Graduate> getOnlineGraduateQueue() {
        return onlineGraduateQueue;
    }

    public Player getController() {
        return controller;
    }

    public void setController(Player player) {
        this.controller = player;
    }

    public Graduate getNextGraduate() {
        // if queue isn't empty, return the next person in the queue
        if (onlineGraduateQueue.size() > 0) {
            return onlineGraduateQueue.remove();
        }

        // queue is empty, so let's pull a random graduate from this ceremony from the database
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT g.id, g.name, pronunciation, degreeLevel, honors, major, seniorQuote, uuid, u.name " +
                    "FROM graduates g join universities u on g.university = u.id " +
                    "WHERE NOT graduated AND ceremony=? " +
                    "ORDER BY RAND() LIMIT 1;");
            stmt.setInt(1, this.id);
            res = stmt.executeQuery();
            if (res.next()) {
                Graduate g = new Graduate(res.getInt(1),
                        this,
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5),
                        res.getString(6),
                        res.getString(7),
                        res.getString(8),
                        res.getString(9),
                        false);
                res.close();
                c.close();
                return g;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
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
