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
