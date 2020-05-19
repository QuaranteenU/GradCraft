package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.command.CommandContext;
import net.citizensnpcs.api.event.CommandSenderCreateNPCEvent;
import net.citizensnpcs.api.event.PlayerCreateNPCEvent;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Objects;

public class GraduateNPC {
    private NPC npc;

    public GraduateNPC(CommandSender sender, Location spawnPoint) {
        EntityType type = EntityType.PLAYER;
        npc = CitizensAPI.getNPCRegistry().createNPC(type, "Dustin Hoffman");

        CommandSenderCreateNPCEvent event = new CommandSenderCreateNPCEvent(sender, npc);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            npc.destroy();
            String reason = "Couldn't create NPC.";
            if (!event.getCancelReason().isEmpty())
                reason += " Reason: " + event.getCancelReason();
            throw new CommandException(reason);
        }

        if (spawnPoint == null) {
            npc.destroy();
            throw new CommandException(Messages.INVALID_SPAWN_LOCATION);
        }

        Iterable<Vector> path = Arrays.asList(
                new Vector(spawnPoint.getX() + 3, spawnPoint.getY() + 5, spawnPoint.getZ() + 3),
                new Vector(spawnPoint.getX() - 3, spawnPoint.getY() - 5, spawnPoint.getZ() - 3),
                new Vector(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ())
        );
        npc.addTrait(GraduateTrait.class);
        npc.spawn(spawnPoint, SpawnReason.CREATE);
        npc.getNavigator().setTarget(path);
    }

    public void destroy() {
        npc.destroy();
    }
}
