/*
    This file is part of GradCraft, by the Quaranteen University team.
    https://quaranteen.university

    GradCraft is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GradCraft is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GradCraft.  If not, see <https://www.gnu.org/licenses/>.
 */

package university.quaranteen.gradcraft.citizens;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import university.quaranteen.gradcraft.GradCraftPlugin;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static university.quaranteen.gradcraft.commands.DbDiplomaCommand.GET_USER_DATA_QUERY;

public class ProfessorTrait extends Trait implements Listener {
    private final GradCraftPlugin plugin;

    public ProfessorTrait() {
        super("professor");
        this.plugin = JavaPlugin.getPlugin(GradCraftPlugin.class);
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        // Confirm that the User Clicked on the Professor
        if (event.getNPC() == this.getNPC()) {
            Player grad = event.getClicker();
            if (grad != null) {
                if(!grad.getInventory().contains(Material.FILLED_MAP)) {
                    try {
                        Connection c = plugin.db.getConnection();
                        PreparedStatement stmt = c.prepareStatement(GET_USER_DATA_QUERY);
                        stmt.setString(1, grad.getUniqueId().toString());
                        ResultSet res = stmt.executeQuery();
                        if (res.next()) {
                            grad.sendMessage("Congratulations, " + grad.getName() + "! Here's your diploma!");
                            String name = res.getString(1);
                            String degreeLevel = res.getString(2);
                            String major = res.getString(3);
                            String school = res.getString(4);
                            boolean isHighschool = res.getBoolean(5);
                            if (isHighschool)
                                grad.getInventory().addItem(new Diploma(grad, name, school, isHighschool).createItem());
                            else
                                grad.getInventory().addItem(new Diploma(grad, name, major, degreeLevel, isHighschool).createItem());
                        } else {
                            grad.sendMessage("You're not a graduate! :(");
                        }
                        res.close();
                        c.close();
                    } catch (SQLException ex) {
                        this.plugin.getLogger().severe(grad.getDisplayName() + " \"" + grad.getUniqueId().toString() + "\" - diploma failed");
                        ex.printStackTrace();
                    }
                } else {
                    grad.sendMessage("You've already received your diploma!");
                }
            }
        }
    }

    @Override
    public void onSpawn() {
        setEquipment();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.config.getString("gradWorld")));
        Location facePoint = new Location(
                world,
                plugin.config.getDouble("gradFacePoint.x"),
                plugin.config.getDouble("gradFacePoint.y"),
                plugin.config.getDouble("gradFacePoint.z"));
        npc.faceLocation(facePoint);
    }

    @Override
    public void onDespawn() {
        NPCRightClickEvent.getHandlerList().unregister(this);
    }

    public void setEquipment() {
        npc.addTrait(Equipment.class);
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.DIAMOND_LEGGINGS));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.DIAMOND_CHESTPLATE));
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.DIAMOND_HELMET));
    }

    @Override
    public void onAttach() {
        plugin.getLogger().info(npc.getName() + " has been assigned ProfessorTrait!");
    }
}
