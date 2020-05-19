package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;

public class GraduateNPC {
    public void TestMethod() {
        EntityType type = EntityType.PLAYER;
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(type, "Waka waka man");
    }
}
