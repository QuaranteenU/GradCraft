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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.util.List;
import java.util.UUID;

public class StageController {
    private final World world;
    private Graduate currentGraduate;
    private final RegionContainer wgRegionContainer;
    private final RegionManager wgRegionManager;
    private final ProtectedRegion stageRegion;
    private final Location tpInLocation;
    private final Location tpOutLocation;

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
    }

    public void nextGraduate(Graduate g) {
        currentGraduate = g;
    }

    public void notifyShowRunner(Player showRunner) {
        Graduate g = currentGraduate;

        MessageBuilder m = new MessageBuilder()
                .dark_gray("===== ");

        long delay = g.getShowDelay().toSeconds();
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
                .green(g.getUniversityName())
                .newLine();

        if (g.getHonors() != null) {
            m.yellow(g.getHonors())
                    .newLine();
        }

        showRunner.sendMessage(m.toString());
    }

    public void notifySpectators() {
        List<Player> players = this.world.getPlayers();
        Title t = Title.builder().stay(5)
                .title(new MessageBuilder()
                        .aqua(currentGraduate.getName())
                        .toString())
                .subtitle(new MessageBuilder()
                        .blue(currentGraduate.getDegreeLevel())
                        .white(" - ")
                        .blue(currentGraduate.getMajor())
                        .newLine()
                        .aqua(currentGraduate.getUniversityName())
                        .toString())
                .build();
        for (Player p : players) {
            p.sendTitle(t);
        }
    }

    public void forceOutGraduate() {
        currentGraduate.getPlayer().teleport(tpOutLocation);
        stageRegion.getMembers().removePlayer(currentGraduate.getUuid());
    }

    public void teleportInGraduate() {
        Player p = currentGraduate.getPlayer();
        if (p != null && p.isOnline()) {
            // player is online, so add them to the stage region and tp them in
            stageRegion.getMembers().addPlayer(currentGraduate.getUuid());
            p.teleport(tpInLocation);
            p.sendMessage(new MessageBuilder()
                    .green("Congratulations! Right click on Professor Steve for your diploma!")
                    .toString());
        } else {
            //TODO: handling for NPC
        }
    }

    public Graduate getCurrentGraduate() {
        return currentGraduate;
    }

    public World getWorld() {
        return world;
    }
}
