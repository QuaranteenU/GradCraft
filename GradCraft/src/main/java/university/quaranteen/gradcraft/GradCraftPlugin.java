package university.quaranteen.gradcraft;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import kr.entree.spigradle.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Properties;

@Plugin
public class GradCraftPlugin extends PluginBase {
    public FileConfiguration config;
    public EbeanServer db;

    @Override
    public int getMinimumLibVersion() {
        return Common.VERSION;
    }

    @Override
    public void enable() {
        // save default config if it doesn't exist already
        saveDefaultConfig();

        // connect to database
        ServerConfig cfg = new ServerConfig();

        Properties dbconfig = new Properties();
        dbconfig.put("ebean.db.ddl.generate", "true");
        dbconfig.put("ebean.db.ddl.run", "true");
        dbconfig.put("datasource.db.username", config.getString("db.username", "root"));
        dbconfig.put("datasource.db.password", config.getString("db.password", ""));
        dbconfig.put("datasource.db.databaseUrl", config.getString("db.server", "jdbc:mysql://localhost:3306/graduation"));
        dbconfig.put("datasource.db.databaseDriver", config.getString("db.driver", "com.mysql.jdbc.Driver"));

        cfg.loadFromProperties(dbconfig);
        db = EbeanServerFactory.create(cfg);

        getLogger().info("GradCraft initialized!");
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
