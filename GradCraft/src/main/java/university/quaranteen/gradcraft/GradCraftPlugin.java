package university.quaranteen.gradcraft;

import kr.entree.spigradle.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin
public class GradCraftPlugin extends JavaPlugin {
    @Override
    public void onDisable() {
        getLogger().info("GradCraft is disabled!");
    }

    @Override
    public void onEnable() {
        getLogger().info("GradCraft is loaded!");
    }
}
