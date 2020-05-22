package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.GradCraftPlugin;

public class ProfessorNPC {
    private final GradCraftPlugin plugin;
    private NPC npc;

    public static final String uuid = "66a68bc0-2e3c-40ce-984c-3153646d1694";
    public static final String signature = "iCp20y1BzuwlIIM9TR0r0UQf2dKnM5h0qSVVBmOoN3d4NS9dWVKoY2IyYkb6BukF6XjQ/HgdXqNGEdJpggUJqUd67acFhZhLV6XXpAWg2scBC5cm/sbvC4c3Dj3B8/xWtjyZpM4dv12NOBbQAcZBFWYU1pSmwyJRkF2bcBwcD/WkrQqM8OvvkSrPsYWg2ZHgJPOElIbGr2Qbo+cTCLK8cEaruWSDT9PGzqLp0YKr6r4zonA0H08fJtNrKGY6tx8NkpJWA/5OTgsyldbChQmIUUVG1rxM9NnOBX9HsWe2WGt/FJ7yI0vHgML+dSmrjA071nZ3+Arue6I6XowjcUaWndQ2g/POhqMHkohIqXGgmXio0oGgZSW7gbDWztB/s52n4Xt/LEraypLzHRbwyYLqwW12fpjwq7sUE47UjteUO3QmXu/ZFAVpdVjkMRNS038MrZIJeoTXK3FtkmTl6LIucXfYA6mK7yUF8ueacgT0rzrb4VQKXSW9jAnAYPFclaiITkcZS1mx1mTSftAWQYaCzPvIyNycE9aEAjxWQHwJ6GMR6VXAF6LbIbaLtqBJw9DoksUw3CX7Hszwsw06ec4n+WG1LUmPWXRuPszdiTGQFmXIGZeAlDKahTDIHYlKvXgAg84mVVY/qIxAqcmPwwhtbhYut9pz5rRUNH2NjCCgHyc=";
    public static final String textureEncoded = "eyJ0aW1lc3RhbXAiOjE1Nzc3NDc3MDM2NzQsInByb2ZpbGVJZCI6ImMxYWYxODI5MDYwZTQ0OGRhNjYwOWRmZGM2OGEzOWE4IiwicHJvZmlsZU5hbWUiOiJCQVJLeDQiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOWFiMzQ4M2Y4MTA2ZWNjOWU3NmJkNDdjNzEzMTJiMGYxNmE1ODc4NGQ2MDY4NjRmM2IzZTljYjFmZDdiNmMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==";


    public ProfessorNPC(Location spawnPoint) {
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
        EntityType type = EntityType.PLAYER;
        npc = CitizensAPI.getNPCRegistry().createNPC(type, "Professor Steve");

        if (spawnPoint == null) {
            npc.destroy();
            throw new CommandException(Messages.INVALID_SPAWN_LOCATION);
        }

        npc.addTrait(ProfessorTrait.class);
        setSkin();
        npc.spawn(spawnPoint, SpawnReason.CREATE);
    }

    private void setSkin() {
        final SkinTrait trait = npc.getTrait(SkinTrait.class);
        Bukkit.getScheduler().runTask(CitizensAPI.getPlugin(), () -> trait.setSkinPersistent(uuid, signature, textureEncoded));
    }

    public void destroy() {
        npc.destroy();
    }
}
