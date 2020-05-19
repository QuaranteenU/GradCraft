package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class GraduateTrait extends Trait {
    private final GradCraftPlugin plugin;

    public GraduateTrait(GradCraftPlugin plugin) {
        super("graduate");
        this.plugin = plugin;
    }

    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + "has been assigned GraduateTrait!");
    }
}
