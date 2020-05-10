package university.quaranteen.gradcraft;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kr.entree.spigradle.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import university.quaranteen.gradcraft.commands.DbDiplomaCommand;
import university.quaranteen.gradcraft.commands.DiplomaCommand;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.util.Properties;

@Plugin
public class GradCraftPlugin extends PluginBase {
    public FileConfiguration config;

    public HikariDataSource db;

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

        this.getCommand("diploma").setExecutor(new DiplomaCommand());
        this.getCommand("dbdiploma").setExecutor(new DbDiplomaCommand(this));
    }

    @Override
    public void disable() {
        getLogger().info("GradCraft disabled!");
    }

    @Override
    public boolean command(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
