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

package university.quaranteen.gradcraft;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.comphenix.protocol.ProtocolLibrary;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.entree.spigradle.Plugin;
import net.citizensnpcs.trait.LookAtGradTrait;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;
import university.quaranteen.gradcraft.ceremony.ActiveCeremony;
import university.quaranteen.gradcraft.ceremony.CeremonyTimer;
import university.quaranteen.gradcraft.ceremony.commands.*;
import university.quaranteen.gradcraft.commands.DbDiplomaCommand;
import university.quaranteen.gradcraft.commands.RobesCommand;
import university.quaranteen.gradcraft.nametags.NametagListener;
import university.quaranteen.gradcraft.citizens.*;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

@Plugin
public class GradCraftPlugin extends PluginBase {
    public FileConfiguration config;

    public HikariDataSource db;

    public ActiveCeremony ceremony;

    public BukkitTask ceremonyTimerTask;

    //private NametagListener nametagListener;

    @Override
    public int getMinimumLibVersion() {
        return Common.VERSION;
    }

    @Override
    public void enable() {
        // save default config if it doesn't exist already
        saveDefaultConfig();

        config = getConfig();

        // connect to database
        HikariConfig dbconfig = new HikariConfig();
        dbconfig.setJdbcUrl(config.getString("db.server", "jdbc:mysql://localhost:3306/gradcraft?serverTimezone=UTC"));
        dbconfig.setUsername(config.getString("db.username", "root"));
        dbconfig.setPassword(config.getString("db.password", "asdfasdf"));
        dbconfig.addDataSourceProperty("characterEncoding", "UTF-8");
        dbconfig.addDataSourceProperty("useUnicode", "true");
        dbconfig.setPoolName("gradcraft");
        //dbconfig.setDataSourceClassName(config.getString("db.datasource", "com.mysql.cj.jdbc.MysqlDataSource"));

        db = new HikariDataSource(dbconfig);

        getLogger().info("GradCraft initialized!");

        // register commands
        this.getCommand("diploma").setExecutor(new DbDiplomaCommand(this));
        this.getCommand("robes").setExecutor(new RobesCommand(this));

        // ceremony commands
        this.getCommand("cerclaim").setExecutor(new ClaimCommand(this));
        this.getCommand("cernext").setExecutor(new NextCommand(this));
        this.getCommand("cerstart").setExecutor(new StartCommand(this));
        this.getCommand("cerstop").setExecutor(new StopCommand(this));
        this.getCommand("cerstatus").setExecutor(new StatusCommand(this));
        this.getCommand("cerlist").setExecutor(new ListCommand(this));


        this.getServer().getScheduler().runTaskTimer(this, new CeremonyTimer(this), 20, 20);

        //nametagListener = new NametagListener(this);
        //this.register(nametagListener);
        //ProtocolLibrary.getProtocolManager().addPacketListener(nametagListener);

        // Register your trait with Citizens
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(GraduateTrait.class).withName("graduate"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ProfessorTrait.class).withName("professor"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(LookAtGradTrait.class).withName("lookatgrad"));
    }

    @Override
    public void disable() {
        getLogger().info("GradCraft disabled!");
        if (ceremonyTimerTask != null)
            this.getServer().getScheduler().cancelTask(ceremonyTimerTask.getTaskId());
        if (db != null)
            db.close();

        //if (nametagListener != null)
        //    ProtocolLibrary.getProtocolManager().removePacketListener(nametagListener);

        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(GraduateTrait.class).withName("graduate"));
        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(ProfessorTrait.class).withName("professor"));
        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(LookAtGradTrait.class).withName("lookatgrad"));
    }

    @Override
    public boolean command(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
