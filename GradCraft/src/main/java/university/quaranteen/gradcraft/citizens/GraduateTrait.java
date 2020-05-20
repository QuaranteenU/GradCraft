package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.nms.v1_11_R1.entity.EntityHumanNPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.util.Arrays;
import java.util.Objects;


public class GraduateTrait extends Trait {
    private final GradCraftPlugin plugin;

    public GraduateTrait() {
        super("graduate");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @Override
    public void onSpawn() {
        setEquipment();

        World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
        Location test = new Location(
                world,
                plugin.config.getDouble("gradTpPoint.x"),
                plugin.config.getDouble("gradTpPoint.y"),
                plugin.config.getDouble("gradTpPoint.z"));
        Navigator npcNav = npc.getNavigator();
        //npcNav.getLocalParameters().range((float) 100);
        npcNav.getLocalParameters().useNewPathfinder(true);
        Iterable<Vector> path = Arrays.asList(
                new Vector(test.getX(),test.getY(),test.getZ()+10),
                new Vector(test.getX(),test.getY(),test.getZ()-5)
        );
        npcNav.setTarget(path);
        addSingleUseCallback(NavigatorCallback callback)
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
