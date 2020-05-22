package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.util.Objects;

public class GraduateTrait extends Trait {
    private final GradCraftPlugin plugin;
    private boolean hasDiploma = false;
    private boolean reachedCenter = false;
    private boolean reachedEnd = false;

    public GraduateTrait() {
        super("graduate");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @Override
    public void onSpawn() {
        setEquipment();

        World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
        Navigator npcNav = npc.getNavigator();
        npcNav.getLocalParameters().useNewPathfinder(true);
        npcNav.getLocalParameters().range((float) 100);
        Location lectern = new Location(
                world,
                plugin.config.getDouble("gradCenterPoint.x"),
                plugin.config.getDouble("gradCenterPoint.y"),
                plugin.config.getDouble("gradCenterPoint.z"));
        npcNav.setTarget(lectern);
    }

    @EventHandler
    public void onNavigationEnd(NavigationCompleteEvent event) {
        if (!reachedCenter) {
            reachedCenter = true;

            World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
            Location facePoint = new Location(
                    world,
                    plugin.config.getDouble("gradFacePoint.x"),
                    plugin.config.getDouble("gradFacePoint.y"),
                    plugin.config.getDouble("gradFacePoint.z"));
            npc.faceLocation(facePoint);

            if (!hasDiploma) {
                npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, new ItemStack(Material.FILLED_MAP));
                hasDiploma = true;
            }

            npc.getEntity().setVelocity(npc.getEntity().getVelocity().add(new Vector(0,0.4,0)));

            Bukkit.getScheduler().scheduleSyncDelayedTask(CitizensAPI.getPlugin(), () -> {
                Navigator npcNav = npc.getNavigator();
                npcNav.getLocalParameters().useNewPathfinder(true);
                npcNav.getLocalParameters().range((float) 100);
                Location endPoint = new Location(
                        world,
                        plugin.config.getDouble("gradEndPoint.x"),
                        plugin.config.getDouble("gradEndPoint.y"),
                        plugin.config.getDouble("gradEndPoint.z"));
                npcNav.setTarget(endPoint);
            }, 80);
        } else if (!reachedEnd) {
            reachedEnd = true;
            Bukkit.getScheduler().scheduleSyncDelayedTask(CitizensAPI.getPlugin(), () -> {
                npc.despawn();
            }, 10);
        }
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
