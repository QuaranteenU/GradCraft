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
    public Diploma() {}

    public ItemStack createItem(Player owner) {
        ItemStack item = MapDisplay.createMapItem(DiplomaMapDisplay.class);
        CommonTagCompound tag = ItemUtil.getMetaTag(item);
        tag.putUUID("diplomaOwner", owner.getUniqueId());
        tag.putValue("diplomaOwnerName", owner.getDisplayName());

        List<String> lore = Arrays.asList("Diploma Lore Text", "Insert Stuff Here");

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Diploma");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
