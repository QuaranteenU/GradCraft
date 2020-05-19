package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class ProfessorTrait extends Trait {
    private final GradCraftPlugin plugin;

    public ProfessorTrait(GradCraftPlugin plugin) {
        super("professor");
        this.plugin = plugin;
    }

    @EventHandler
    public void click(NPCClickEvent event) {
        // Confirm that the User Clicked on the Professor
        if (event.getNPC() == this.getNPC()) {
            // event.clicker is the user who clicked on the Professor
            // TODO: Give User the Diploma
            plugin.getServer().getLogger().info(npc.getName() + "has been clicked on!");
        }
    }

    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + "has been assigned ProfessorTrait!");
    }
}
