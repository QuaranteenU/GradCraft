package university.quaranteen.gradcraft.diploma;

import com.bergerkiller.bukkit.common.map.*;

public class DiplomaMapDisplay extends MapDisplay {
    @Override
    public void onAttached() {
        MapTexture bg = this.loadTexture("university/quaranteen/gradcraft/textures/diploma_bg.png");
        this.getLayer().draw(bg, 0, 0);

        renderDiploma();
    }

    @Override
    public void onMapItemChanged() {
        this.renderDiploma();
    }

    private void renderDiploma() {
        this.getLayer(1).clear();
        this.getLayer(1).draw(MapFont.MINECRAFT, 10, 40, MapColorPalette.COLOR_BLACK, "Diploma");
    }
}
