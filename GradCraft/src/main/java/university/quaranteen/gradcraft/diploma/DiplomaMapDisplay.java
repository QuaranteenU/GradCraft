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

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.nbt.CommonTagCompound;
import com.bergerkiller.bukkit.common.utils.ItemUtil;

public class DiplomaMapDisplay extends MapDisplay {
    @Override
    public void onAttached() {
        CommonTagCompound mapData = ItemUtil.getMetaTag(this.getMapItem());
        boolean isHighschool = mapData.getValue(Diploma.DIPLOMA_IS_HS_FIELD, Boolean.class);
        MapTexture bg;
        if (isHighschool) {
            bg = this.loadTexture("diploma_bg_QUA.png");
        } else {
            bg = this.loadTexture("diploma_bg_QU.png");
        }
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

        String[] name = splitIntoLines(mapData.getValue(Diploma.DIPLOMA_NAME_FIELD, String.class));
        String major = mapData.getValue(Diploma.DIPLOMA_MAJOR_FIELD, String.class);
        String level = mapData.getValue(Diploma.DIPLOMA_LEVEL_FIELD, String.class);
        String school = mapData.getValue(Diploma.DIPLOMA_SCHOOL_FIELD, String.class);
        boolean isHighschool = mapData.getValue(Diploma.DIPLOMA_IS_HS_FIELD, Boolean.class);

        if (isHighschool) {
            this.getLayer(1).setAlignment(MapFont.Alignment.MIDDLE);
            if (name.length == 2) {
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 65, MapColorPalette.COLOR_BLACK, name[0]);
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 75, MapColorPalette.COLOR_BLACK, name[1]);
            } else if (name.length == 1) {
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 70, MapColorPalette.COLOR_BLACK, name[0]);
            }
        } else {
            this.getLayer(1).setAlignment(MapFont.Alignment.MIDDLE);
            if (name.length == 2) {
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 50, MapColorPalette.COLOR_BLACK, name[0]);
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 60, MapColorPalette.COLOR_BLACK, name[1]);
            } else if (name.length == 1) {
                this.getLayer(1).draw(MapFont.MINECRAFT, 64, 55, MapColorPalette.COLOR_BLACK, name[0]);
            }

            if (major.length() < 40) { // (128px - 6px border) / 3 px/char = 40 chars max
                this.getLayer(1).draw(MapFont.TINY, 64, 83, MapColorPalette.COLOR_BLACK, level);
                this.getLayer(1).draw(MapFont.TINY, 64, 91, MapColorPalette.COLOR_BLACK, major);
            } else {
                String[] majorLines = splitIntoLines(major);
                if (majorLines.length == 2) {
                    this.getLayer(1).draw(MapFont.TINY, 64, 83, MapColorPalette.COLOR_BLACK, majorLines[0]);
                    this.getLayer(1).draw(MapFont.TINY, 64, 91, MapColorPalette.COLOR_BLACK, majorLines[1]);
                }
            }
        }
    }

    private static String[] splitIntoLines(String from) {
        // split the string from into 2 lines of roughly equal length
        String[] nameParts = from.split(" ");
        if (nameParts.length < 2) {
            return new String[] { from };
        }
        int[] partLength = new int[nameParts.length];
        int totalLength = from.length();
        int len1 = totalLength, len2 = 0, split = nameParts.length - 1;

        for (int i = 0; i < nameParts.length; i++) {
            partLength[i] = nameParts[i].length();
        }

        for (int i = nameParts.length - 1; i >= 0; i--) {
            if (Math.abs((len1 - partLength[i]) - (len2 + partLength[i])) > Math.abs(len1 - len2)) {
                break;
            } else {
                len1 -= partLength[i];
                len2 += partLength[i];
                split--;
            }
        }

        String[] toReturn = new String[] {"", ""};
        for (int i = 0; i < nameParts.length; i++) {
            if (i <= split) {
                toReturn[0] += nameParts[i] + (i == split ? "" : " ");
            } else {
                toReturn[1] += nameParts[i] + (i + 1 == nameParts.length ? "" : " ");
            }
        }

        return toReturn;
    }
}
