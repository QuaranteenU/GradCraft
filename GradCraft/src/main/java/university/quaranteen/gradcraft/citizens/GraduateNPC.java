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
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class GraduateNPC {
    private NPC npc;

    public GraduateNPC(CommandSender sender, Location spawnPoint) {
        EntityType type = EntityType.PLAYER;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(type, "Dustin Hoffman");

        CommandSenderCreateNPCEvent event = new CommandSenderCreateNPCEvent(sender, this.npc);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            this.npc.destroy();
            String reason = "Couldn't create NPC.";
            if (!event.getCancelReason().isEmpty())
                reason += " Reason: " + event.getCancelReason();
            throw new CommandException(reason);
        }

        if (spawnPoint == null) {
            this.npc.destroy();
            throw new CommandException(Messages.INVALID_SPAWN_LOCATION);
        }

        this.npc.spawn(spawnPoint, SpawnReason.CREATE);
        this.npc.addTrait(GraduateTrait.class);
    }

    public void destroy() {
        this.npc.destroy();
    }
}
