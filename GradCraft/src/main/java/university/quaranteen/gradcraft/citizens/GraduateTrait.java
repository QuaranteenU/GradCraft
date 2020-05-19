package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.GradCraftPlugin;


public class GraduateTrait extends Trait {
    private final GradCraftPlugin plugin;

    public GraduateTrait() {
        super("graduate");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @Override
    public void onSpawn() {
        setEquipment();
    }

    public void setEquipment() {
        npc.addTrait(Equipment.class);
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.DIAMOND_LEGGINGS));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.DIAMOND_CHESTPLATE));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.DIAMOND_HELMET));
    }

    @Override
    public void onAttach() {
        plugin.getLogger().info(npc.getName() + " has been assigned GraduateTrait!");
    }
}
