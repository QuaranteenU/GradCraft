package university.quaranteen.gradcraft.ceremony;

import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import org.bukkit.entity.Player;
import university.quaranteen.gradcraft.diploma.Diploma;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

public class Graduate {
    public Graduate(int id, ActiveCeremony ceremony, String name, String pronunciation, String degreeLevel, String honors, String major, String seniorQuote, UUID uuid, String universityName, boolean graduated, Timestamp timeslot) {
        this.id = id;
        this.name = name;
        this.pronunciation = pronunciation;
        this.degreeLevel = degreeLevel;
        this.honors = honors;
        this.major = major;
        this.seniorQuote = seniorQuote;
        this.universityName = universityName;
        this.graduated = graduated;
        this.ceremony = ceremony;
        if (uuid == null)
            this.uuid = Graduate.STEVE_UUID;
        else
            this.uuid = uuid;
        this.timeslot = timeslot;
    }

    public Graduate(int id, ActiveCeremony ceremony, String name, String pronunciation, String degreeLevel, String honors, String major, String seniorQuote, String uuid, String universityName, boolean graduated, Timestamp timeslot) {
        this.id = id;
        this.name = name;
        this.pronunciation = pronunciation;
        this.degreeLevel = degreeLevel;
        this.honors = honors;
        this.major = major;
        this.seniorQuote = seniorQuote;
        this.universityName = universityName;
        this.graduated = graduated;
        this.ceremony = ceremony;
        if (uuid == null)
            this.uuid = Graduate.STEVE_UUID;
        else
            this.uuid = UUID.fromString(uuid);
        this.timeslot = timeslot;
    }

    // this is my skin lol someone please change it
    public static final UUID STEVE_UUID = UUID.fromString("07a6fd4b-3c03-4df7-9c4e-951d1d57c58b");

    private int id;
    private String name;
    private String pronunciation;
    private String degreeLevel;
    private String honors;
    private String major;
    private String seniorQuote;
    private String universityName;
    private ActiveCeremony ceremony;
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

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        if (uuid == null)
            return null;
        return (Player) PlayerUtil.getEntity(ceremony.getStageController().getWorld(), uuid);
    }

    public String getUniversityName() {
        return universityName;
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
        return p == null || !p.isOnline() ? null : new Diploma(getPlayer(), name, major, degreeLevel);
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