package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class ProfessorTrait extends Trait {
    private final GradCraftPlugin plugin;

    public ProfessorTrait() {
        super("professor");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @EventHandler
    public void click(NPCClickEvent event) {
        // Confirm that the User Clicked on the Professor
        if (event.getNPC() == this.getNPC()) {
            Player grad = event.getClicker();
            PluginCommand cmd = plugin.getCommand("dbdiploma");
            if (cmd != null) {
                cmd.execute(grad, "", new String[]{""});
            } else {
                plugin.getLogger().info(npc.getName() + " is unable to find the diploma command");
            }
        }
    }

    @Override
    public void onSpawn() {
        setEquipment();
    }

    public void setEquipment() {
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.DIAMOND_LEGGINGS));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.DIAMOND_CHESTPLATE));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.DIAMOND_HELMET));
    }

    @Override
    public void onAttach() {
        plugin.getLogger().info(npc.getName() + " has been assigned ProfessorTrait!");
    }
}
