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

package university.quaranteen.gradcraft.nametags;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import university.quaranteen.gradcraft.GradCraftPlugin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NametagListener implements Listener, PacketListener {
    private final HikariDataSource db;
    private final GradCraftPlugin plugin;

    private final HashMap<UUID, String> realNames = new HashMap<>();
    private final HashMap<UUID, String> screenNames = new HashMap<>();

    public NametagListener(GradCraftPlugin plugin) {
        this.plugin = plugin;
        this.db = plugin.db;
    }

    @EventHandler
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
            } else {
                realNames.put(p.getUniqueId(), p.getName());
            }
            res.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        screenNames.remove(p.getUniqueId());
        realNames.remove(p.getUniqueId());
    }

    @EventHandler
    public void playerChangeWorldEvent(PlayerChangedWorldEvent event) {
        World gradWorld = plugin.getServer().getWorld(plugin.config.getString("gradWorld", "world"));
        Player refresher = event.getPlayer();
        if (event.getFrom() != gradWorld && refresher.getWorld() != gradWorld)
            return;

        // force resend player info packets
        refresher.setPlayerProfile(refresher.getPlayerProfile());
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

    @Override
    public void onPacketSending(PacketEvent packetEvent) {
        // many thanks to ricardofalcao on spigotmc forums
        // and dmulloy2's packetwrapper
        // as i write this at 3 am the night before quaranteen eve

        // packetEvent.getPlayer() returns the player
        if (!packetEvent.getPlayer().getWorld().getName().equalsIgnoreCase(plugin.config.getString("gradWorld")))
            return;

        WrapperPlayServerPlayerInfo wrapper = new WrapperPlayServerPlayerInfo(packetEvent.getPacket());
        if (wrapper.getAction() != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
            return;

        List<PlayerInfoData> dataList = new ArrayList<>();
        for (PlayerInfoData data : wrapper.getData()) {
            if (data == null || data.getProfile() == null || data.getProfile().getUUID() == null) {
                dataList.add(data);
                continue;
            }

            WrappedGameProfile profile = data.getProfile();
            String newName = realNames.get(data.getProfile().getUUID());
            if (newName == null) {
                dataList.add(data);
                continue;
            }

            WrappedGameProfile newProfile = profile.withName(newName);
            newProfile.getProperties().putAll(profile.getProperties());

            PlayerInfoData newData = new PlayerInfoData(newProfile, data.getLatency(), data.getGameMode(), data.getDisplayName());
            dataList.add(newData);
        }

        wrapper.setData(dataList);
    }

    @Override
    public void onPacketReceiving(PacketEvent packetEvent) {

    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.newBuilder()
                .lowest()
                .types(PacketType.Play.Server.PLAYER_INFO)
                .gamePhase(GamePhase.BOTH)
                .build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
