package university.quaranteen.gradcraft.model;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import jdk.jfr.Unsigned;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class University extends ModelBase {
    @NotNull @Length(256)
    private String name;

    @NotNull @Length(256)
    private String location;

    @Unsigned
    private int webColor1;

    @Unsigned
    private int webColor2;

    private byte minecraftColor1;

    private byte minecraftColor2;

    @ManyToOne(targetEntity=Graduate.class)
    private List<Graduate> graduates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWebColor1() {
        return webColor1;
    }

    public void setWebColor1(int webColor1) {
        this.webColor1 = webColor1;
    }

    public int getWebColor2() {
        return webColor2;
    }

    public void setWebColor2(int webColor2) {
        this.webColor2 = webColor2;
    }

    public byte getMinecraftColor1() {
        return minecraftColor1;
    }

    public void setMinecraftColor1(byte minecraftColor1) {
        this.minecraftColor1 = minecraftColor1;
    }

    public byte getMinecraftColor2() {
        return minecraftColor2;
    }

    public void setMinecraftColor2(byte minecraftColor2) {
        this.minecraftColor2 = minecraftColor2;
    }

    public List<Graduate> getGraduates() {
        return graduates;
    }

    public void setGraduates(List<Graduate> graduates) {
        this.graduates = graduates;
    }
}
