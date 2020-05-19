package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.trait.Trait;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class GraduateTrait extends Trait {
    private final GradCraftPlugin plugin;

    public GraduateTrait() {
        super("graduate");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @Override
    public void onAttach() {
        plugin.getLogger().info(npc.getName() + " has been assigned GraduateTrait!");
    }
}
