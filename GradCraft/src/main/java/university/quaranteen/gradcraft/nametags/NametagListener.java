package university.quaranteen.gradcraft.nametags;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class NametagListener implements Listener {
    private final HikariDataSource db;
    private final GradCraftPlugin plugin;

    private final HashMap<UUID, String> realNames = new HashMap<>();
    private final HashMap<UUID, String> screenNames = new HashMap<>();

    public NametagListener(GradCraftPlugin plugin) {
        this.plugin = plugin;
        this.db = plugin.db;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        screenNames.put(p.getUniqueId(), p.getName());

        // query db for user
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT name FROM graduates WHERE uuid = ?;");
            stmt.setString(1, uuid.toString());
            res = stmt.executeQuery();
            if (res.next()) {
                plugin.getLogger().info(res.getString(1) + " logged in.");
                String name = getAbbreviatedName(res.getString(1));
                realNames.put(p.getUniqueId(), name);

                if (p.getWorld().getName().equalsIgnoreCase(plugin.config.getString("gradWorld"))) {
                    PlayerProfile profile = p.getPlayerProfile();
                    profile.setName(name);
                    p.setPlayerProfile(profile);

                }
            } else {
                realNames.put(p.getUniqueId(), p.getName());
            }
            res.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerProfile profile = p.getPlayerProfile();
        profile.setName(screenNames.get(p.getUniqueId()));
        p.setPlayerProfile(profile);

        screenNames.remove(p.getUniqueId());
        realNames.remove(p.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        PlayerProfile profile = p.getPlayerProfile();
        if (p.getWorld().getName().equalsIgnoreCase(plugin.config.getString("gradWorld"))) {
            profile.setName(realNames.get(p.getUniqueId()));
        } else {
            profile.setName(screenNames.get(p.getUniqueId()));
        }
        p.setPlayerProfile(profile);
    }

    public static String getAbbreviatedName(String fullName) {
        String newName = fullName;
        String[] nameParts = fullName.split(" ");

        for (int i = nameParts.length - 1; i >= 1; i--) {
            nameParts[i] = nameParts[i].substring(0, 1).toUpperCase() + ".";
            newName = String.join(" ", nameParts);
        }

        if (newName.length() < 16) {
            return newName;
        }

        if (nameParts[0].length() < 16) return nameParts[0];
        else return nameParts[0].substring(0, 16);
    }
}
