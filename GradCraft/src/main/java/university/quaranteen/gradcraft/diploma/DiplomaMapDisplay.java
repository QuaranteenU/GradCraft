package university.quaranteen.gradcraft.diploma;

import com.bergerkiller.bukkit.common.map.*;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;

public class DiplomaMapDisplay extends MapDisplay {
    @Override
    public void onAttached() {
        MapTexture bg = this.loadTexture("diploma_bg.png");
        this.getLayer().draw(bg, 0, 0);

        renderDiploma();
    }

    @Override
    public void onMapItemChanged() {
        this.renderDiploma();
    }

    private void renderDiploma() {
        this.getLayer(1).clear();
        CommonTagCompound mapData = ItemUtil.getMetaTag(this.getMapItem());
        this.getLayer(1).draw(MapFont.MINECRAFT, 10, 40, MapColorPalette.COLOR_BLACK, mapData.getValue(Diploma.DIPLOMA_NAME_FIELD, String.class));
        this.getLayer(1).draw(MapFont.MINECRAFT, 10, 60, MapColorPalette.COLOR_BLACK, mapData.getValue(Diploma.DIPLOMA_MAJOR_FIELD, String.class));
    }
}
