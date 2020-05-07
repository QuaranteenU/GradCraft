package university.quaranteen.gradcraft;

import kr.entree.spigradle.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.commands.DiplomaCommand;

import java.util.Objects;

@Plugin
public class GradCraftPlugin extends JavaPlugin {
    //public static GradCraftPlugin instance;

    @Override
    public void onDisable() {
        //GradCraftPlugin.instance = null;
        getLogger().info("GradCraft is disabled!");
    }

    @Override
    public void onEnable() {
        //GradCraftPlugin.instance = this;
        Objects.requireNonNull(this.getCommand("diploma")).setExecutor(new DiplomaCommand());
        getLogger().info("GradCraft is loaded!");
    }
}
