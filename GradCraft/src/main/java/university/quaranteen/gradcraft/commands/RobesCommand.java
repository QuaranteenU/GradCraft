package university.quaranteen.gradcraft.commands;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import university.quaranteen.gradcraft.GradCraftPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

public class RobesCommand implements CommandExecutor {
    public RobesCommand(GradCraftPlugin plugin) {
        this.db = plugin.db;
        this.validWorld = plugin.config.getString("gradWorld");
    }

    private final HikariDataSource db;
    private final String validWorld;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command doesn't work from console mode.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.getWorld().getName().equals(validWorld)) {
            p.sendMessage("You need to be in the graduation world to get robes!");
            return true;
        }

        if (args.length < 1)
            return false;

        // check to see if user is a graduate
        Connection c;
        ResultSet res;
        try {
            c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT 1 FROM graduates WHERE uuid = ? LIMIT 1;");
            stmt.setString(1, p.getUniqueId().toString());
            res = stmt.executeQuery();
            if (!res.next()) {
                p.sendMessage(new MessageBuilder().red("You're not a graduate!").toString());
                res.close();
                c.close();
                return true;
            }
            res.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return true;
        }

        ItemStack head, chest, pants, air;
        air = new ItemStack(Material.AIR, 0);

        switch (args[0].toLowerCase()) {
            case "red":
                head = new ItemStack(Material.IRON_HELMET, 1);
                chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
                pants = new ItemStack(Material.IRON_LEGGINGS, 1);
                break;
            case "black":
                head = new ItemStack(Material.DIAMOND_HELMET, 1);
                chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                break;
            case "blue":
                head = new ItemStack(Material.GOLDEN_HELMET, 1);
                chest = new ItemStack(Material.GOLDEN_CHESTPLATE, 1);
                pants = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
                break;
            default:
                return false;
        }

        p.sendMessage(
                new MessageBuilder()
                        .green("Enjoy your robes! Make sure you have our resource pack enabled.")
                        .toString()
        );
        p.getInventory().setArmorContents(new ItemStack[] {air, pants, chest, head});

        return true;
    }
}
