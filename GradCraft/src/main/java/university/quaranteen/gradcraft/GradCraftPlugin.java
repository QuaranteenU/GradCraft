package university.quaranteen.gradcraft;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.entree.spigradle.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;
import university.quaranteen.gradcraft.ceremony.ActiveCeremony;
import university.quaranteen.gradcraft.ceremony.CeremonyTimer;
import university.quaranteen.gradcraft.ceremony.commands.*;
import university.quaranteen.gradcraft.commands.DbDiplomaCommand;
import university.quaranteen.gradcraft.commands.DiplomaCommand;
import university.quaranteen.gradcraft.commands.RobesCommand;
import university.quaranteen.gradcraft.diploma.Diploma;
import university.quaranteen.gradcraft.nametags.NametagListener;

import java.util.Properties;

@Plugin
public class GradCraftPlugin extends PluginBase {
    public FileConfiguration config;

    public HikariDataSource db;

    public ActiveCeremony ceremony;

    public BukkitTask ceremonyTimerTask;

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
        dbconfig.setJdbcUrl(config.getString("db.server", "jdbc:mysql://localhost:3306/gradcraft"));
        dbconfig.setUsername(config.getString("db.username", "root"));
        dbconfig.setPassword(config.getString("db.password", "asdfasdf"));
        dbconfig.setPoolName("gradcraft");
        //dbconfig.setDataSourceClassName(config.getString("db.datasource", "com.mysql.cj.jdbc.MysqlDataSource"));

        db = new HikariDataSource(dbconfig);

        getLogger().info("GradCraft initialized!");

        // register commands
        this.getCommand("diploma").setExecutor(new DiplomaCommand());
        this.getCommand("dbdiploma").setExecutor(new DbDiplomaCommand(this));
        this.getCommand("robes").setExecutor(new RobesCommand(this));

        // ceremony commands
        this.getCommand("cerclaim").setExecutor(new ClaimCommand(this));
        this.getCommand("cernext").setExecutor(new NextCommand(this));
        this.getCommand("cerstart").setExecutor(new StartCommand(this));
        this.getCommand("cerstop").setExecutor(new StopCommand(this));
        this.getCommand("cerstatus").setExecutor(new StatusCommand(this));
        this.getCommand("cerlist").setExecutor(new ListCommand(this));

        this.getServer().getScheduler().runTaskTimer(this, new CeremonyTimer(this), 40, 40);

        this.register(new NametagListener(this));
    }

    @Override
    public void disable() {
        getLogger().info("GradCraft disabled!");
        if (ceremonyTimerTask != null)
            this.getServer().getScheduler().cancelTask(ceremonyTimerTask.getTaskId());
        if (db != null)
            db.close();
    }

    @Override
    public boolean command(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
