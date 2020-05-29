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

package university.quaranteen.gradcraft.ceremony;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

public class Graduate {
    public Graduate(int id, ActiveCeremony ceremony, String name, String pronunciation, String degreeLevel, String honors, String major, String seniorQuote, UUID uuid, String schoolName, boolean isHighSchool, boolean graduated, Timestamp timeslot) {
        this.id = id;
        this.name = name;
        this.pronunciation = pronunciation;
        this.degreeLevel = degreeLevel;
        this.honors = honors;
        this.major = major;
        this.seniorQuote = seniorQuote;
        this.schoolName = schoolName;
        this.isHighSchool = isHighSchool;
        this.graduated = graduated;
        this.ceremony = ceremony;
        if (uuid != null)
            this.uuid = uuid;
        this.timeslot = timeslot;
    }

    public Graduate(int id, ActiveCeremony ceremony, String name, String pronunciation, String degreeLevel, String honors, String major, String seniorQuote, String uuid, String schoolName, boolean isHighSchool, boolean graduated, Timestamp timeslot) {
        this.id = id;
        this.name = name;
        this.pronunciation = pronunciation;
        this.degreeLevel = degreeLevel;
        this.honors = honors;
        this.major = major;
        this.seniorQuote = seniorQuote;
        this.schoolName = schoolName;
        this.isHighSchool = isHighSchool;
        this.graduated = graduated;
        this.ceremony = ceremony;
        if (uuid != null)
            this.uuid = UUID.fromString(uuid);
        this.timeslot = timeslot;
    }

    private final int id;
    private final String name;
    private final String pronunciation;
    private final String degreeLevel;
    private final String honors;
    private final String major;
    private final String seniorQuote;
    private final String schoolName;
    private final ActiveCeremony ceremony;
    private final boolean isHighSchool;
    private UUID uuid;
    private boolean graduated;
    private final Timestamp timeslot;

    // getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public String getHonors() {
        return honors;
    }

    public String getMajor() {
        return major;
    }

    public String getSeniorQuote() {
        return seniorQuote;
    }

    public boolean isHighSchool() {
        return isHighSchool;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        if (uuid == null)
            return null;
        return (Player) PlayerUtil.getEntity(ceremony.getStageController().getWorld(), uuid);
    }

    public String getSchoolName() {
        return schoolName;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public boolean isOnline() {
        Player p = getPlayer();
        return p != null && getPlayer().isOnline();
    }

    public Diploma getDiploma() {
        Player p = getPlayer();
        if (p != null && p.isOnline()) {
            return isHighSchool ? new Diploma(getPlayer(), name, schoolName, isHighSchool) : new Diploma(getPlayer(), name, major, degreeLevel, isHighSchool);
        } else {
            return null;
        }
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;

        if (graduated)
            ceremony.signalGraduated(this);
    }

    public Timestamp getTimeslot() {
        return timeslot;
    }

    public Duration getShowDelay() {
        return Duration.ofMillis(System.currentTimeMillis() - timeslot.getTime());
    }
}
