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

package university.quaranteen.gradcraft.ceremony;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.World;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.sql.*;

public class ActiveCeremony {
    public ActiveCeremony(int id, World world, GradCraftPlugin plugin) {
        this.id = id;
        this.db = plugin.db;
        this.stageController = new StageController(world, plugin);
    }

    private final int id;
    private World world;
    private Player controller;
    private final HikariDataSource db;
    private Graduate currentGraduate;
    private final StageController stageController;

    public int getId() {
        return id;
    }

    public World getWorld() {
        return stageController.getWorld();
    }

    public Player getShowRunner() {
        return controller;
    }

    public StageController getStageController() { return stageController; }

    public Graduate getCurrentGraduate() {
        return currentGraduate;
    }

    public void setCurrentGraduate(Graduate g) {
        this.currentGraduate = g;
        this.stageController.nextGraduate(g);
    }

    public void setController(Player player) {
        this.controller = player;
    }

    public Graduate getNextGraduate() {
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT g.id, g.name, pronunciation, degreeLevel, honors,  major, seniorQuote, uuid, u.name, isHighSchool, graduated, timeslot " +
                    "FROM graduates g JOIN universities u ON g.university = u.id " +
                    "WHERE NOT graduated AND ceremony = ? " +
                    "ORDER BY timeslot ASC "+
                    "LIMIT 1;");
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
                        res.getBoolean(11),
                        res.getTimestamp(12)
                );
            }
            res.close();
            c.close();

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
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
