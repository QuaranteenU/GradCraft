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

package university.quaranteen.gradcraft.diploma;

import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Diploma {
    public static final String DIPLOMA_OWNER_FIELD = "diplomaOwner";
    public static final String DIPLOMA_NAME_FIELD = "diplomaOwnerName";
    public static final String DIPLOMA_MAJOR_FIELD = "diplomaMajor";
    public static final String DIPLOMA_LEVEL_FIELD = "diplomaLevel";
    private final Player owner;
    private final String name;
    private final String major;
    private final String level;

    public Diploma(Player owner, String name, String major, String level) {
        this.owner = owner;
        this.name = name;
        this.major = major;
        this.level = level;
    }

    public ItemStack createItem() {
        ItemStack item = MapDisplay.createMapItem(DiplomaMapDisplay.class);
        CommonTagCompound tag = ItemUtil.getMetaTag(item);
        tag.putUUID(DIPLOMA_OWNER_FIELD, owner.getUniqueId());
        tag.putValue(DIPLOMA_NAME_FIELD, name);
        tag.putValue(DIPLOMA_MAJOR_FIELD, major);
        tag.putValue(DIPLOMA_LEVEL_FIELD, level);

        List<String> lore = Arrays.asList(name, level, major);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Diploma");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
