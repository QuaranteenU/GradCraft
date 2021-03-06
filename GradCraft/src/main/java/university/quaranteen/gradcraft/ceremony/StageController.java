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

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.destroystokyo.paper.Title;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.citizens.GraduateNPC;
import university.quaranteen.gradcraft.citizens.ProfessorNPC;

import java.util.List;

public class StageController {
    private final World world;
    private Graduate currentGraduate;
    private GraduateNPC currentGraduateNPC;
    private ProfessorNPC professorNPC;
    private final RegionContainer wgRegionContainer;
    private final RegionManager wgRegionManager;
    private final ProtectedRegion stageRegion;
    private final Location tpInLocation;
    private final Location tpOutLocation;
    private final Location professorLocation;

    public StageController(World gradWorld, GradCraftPlugin plugin) {
        this.world = gradWorld;

        WorldGuardPlatform instance = WorldGuard.getInstance().getPlatform();
        wgRegionContainer = instance.getRegionContainer();
        wgRegionManager = wgRegionContainer.get(BukkitAdapter.adapt(world));
        stageRegion = wgRegionManager.getRegion(plugin.config.getString("stageRegion"));

        if (stageRegion == null) {
            throw new NullPointerException("The stage region specified doesn't exist. Please check GradCraft config.yml or your WorldGuard region definition!");
        }

        stageRegion.setFlag(Flags.ENTRY, StateFlag.State.DENY);
        stageRegion.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

        tpInLocation = new Location(
                world,
                plugin.config.getDouble("gradTpPoint.x"),
                plugin.config.getDouble("gradTpPoint.y"),
                plugin.config.getDouble("gradTpPoint.z"));

        tpOutLocation = new Location(
                world,
                plugin.config.getDouble("gradTpOutPoint.x"),
                plugin.config.getDouble("gradTpOutPoint.y"),
                plugin.config.getDouble("gradTpOutPoint.z"));

        professorLocation = new Location(
                world,
                plugin.config.getDouble("degreeNpcPoint.x"),
                plugin.config.getDouble("degreeNpcPoint.y"),
                plugin.config.getDouble("degreeNpcPoint.z"));
    }

    public void startCeremony(int id, Player showRunner) {
        if (professorNPC == null) {
            professorNPC = new ProfessorNPC(professorLocation);
        }

        MessageBuilder m = new MessageBuilder().dark_gray("=====");
        m.newLine().green("Starting ceremony " + id + "!");
        m.newLine().dark_gray("=====");
        showRunner.sendMessage(m.toString());
    }

    public void stopCeremony(int id, Player showRunner) {
        if (professorNPC != null) professorNPC.destroy();
        if (currentGraduateNPC != null) currentGraduateNPC.destroy();

        MessageBuilder m = new MessageBuilder().dark_gray("=====");
        m.newLine().red("Ceremony " + id + " over!");
        m.newLine().dark_gray("=====");
        showRunner.sendMessage(m.toString());
    }

    public void nextGraduate(Graduate g) {
        currentGraduate = g;
    }

    public void notifyShowRunner(Player showRunner) {
        Graduate g = currentGraduate;

        MessageBuilder m = new MessageBuilder()
                .dark_gray("===== ");

        long delay = g.getShowDelay().getSeconds();
        if (delay == 0) {
            m.green("On time");
        } else if (delay > 0) {
            m.yellow(delay, " seconds behind schedule");
        } else {
            m.red(delay, " seconds ahead of schedule");
        }

        m.newLine()
                .green(g.getName());

        if (g.getPronunciation() != null) {
            m.green(" (")
                    .green(g.getPronunciation())
                    .green(")");
        }

        m.newLine()
                .green(g.getDegreeLevel() + " - " + g.getMajor())
                .newLine()
                .green(g.getSchoolName())
                .newLine();

        if (g.getHonors() != null) {
            m.yellow(g.getHonors())
                    .newLine();
        }

        showRunner.sendMessage(m.toString());
    }

    public void notifySpectators() {
        List<Player> players = this.world.getPlayers();
        Title t;
        Title.Builder builder = Title.builder().stay(60)
                .title(new MessageBuilder()
                        .aqua(currentGraduate.getName())
                        .toString());

        if (currentGraduate.isHighSchool())
            builder = builder.subtitle(new MessageBuilder()
                    .blue(currentGraduate.getSchoolName())
                    .toString().replaceAll("\n", ""));
        else
            builder = builder.subtitle(new MessageBuilder()
                .blue(currentGraduate.getDegreeLevel())
                .white(" - ")
                .blue(currentGraduate.getMajor())
                .toString().replaceAll("\n", ""));

        t = builder.build();

        // trying this out - all formatting applies to the last item appended
        ComponentBuilder msg = new ComponentBuilder();
        msg.append(currentGraduate.getName() + "\n").bold(true).color(ChatColor.AQUA);
        if (!currentGraduate.isHighSchool()) {
            msg.append(currentGraduate.getDegreeLevel()).bold(false).color(ChatColor.BLUE);
            msg.append(" - ").color(ChatColor.WHITE);
            msg.append(currentGraduate.getMajor() + "\n").color(ChatColor.BLUE);
        }
        msg.append(currentGraduate.getSchoolName()).color(ChatColor.BLUE);

        BaseComponent[] toSend = msg.getParts().toArray(new BaseComponent[0]);

        for (Player p : players) {
            p.sendTitle(t);
            p.sendMessage(toSend);
        }
    }

    public void forceOutGraduate() {
        if (currentGraduate == null) return;
        if (currentGraduateNPC != null) {
            currentGraduateNPC.destroy();
            currentGraduateNPC = null;
        }

        Player p = currentGraduate.getPlayer();
        if (p != null
                && p.isOnline()
                && p.getWorld() == world
                && stageRegion.contains(BukkitAdapter.asBlockVector(p.getLocation()))) {
            p.teleport(tpOutLocation, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
        }
        if (currentGraduate.getUuid() != null) stageRegion.getMembers().removePlayer(currentGraduate.getUuid());
    }

    public void teleportInGraduate() {
        Player p = currentGraduate.getPlayer();
        if (p != null && p.isOnline()) {
            // player is online, so add them to the stage region and tp them in
            stageRegion.getMembers().addPlayer(currentGraduate.getUuid());
            p.teleport(tpInLocation, PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
            p.sendMessage(new MessageBuilder()
                    .green("Congratulations! Right click on Professor Steve for your diploma!")
                    .toString());
        } else {
            // use NPC
            if (currentGraduateNPC != null) {
                currentGraduateNPC.destroy();
                currentGraduateNPC = null;
            }
            currentGraduateNPC = new GraduateNPC(tpInLocation, currentGraduate);
        }
    }

    public Graduate getCurrentGraduate() {
        return currentGraduate;
    }

    public World getWorld() {
        return world;
    }
}
