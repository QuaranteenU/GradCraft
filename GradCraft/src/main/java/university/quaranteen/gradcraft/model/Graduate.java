package university.quaranteen.gradcraft.model;

import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class Graduate extends ModelBase {
    public Graduate(String name, String email, DegreeLevel degreeLevel, String major, University university, Ceremony ceremony) {
        super();
        this.name = name;
        this.email = email;
        this.degreeLevel = degreeLevel;
        this.major = major;
        this.university = university;
        this.ceremony = ceremony;
    }

    @NotNull @Length(256)
    private String name;

    @Length(256)
    private String pronunciation;

    @NotNull @Length(256)
    private String email;

    @NotNull @Enumerated(EnumType.ORDINAL)
    private DegreeLevel degreeLevel;

    @Length(1024)
    private String honors;

    @NotNull @Length(256)
    private String major;

    @ManyToOne(optional=false)
    private University university;

    @ManyToOne(optional=false)
    private Ceremony ceremony;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public String getHonors() {
        return honors;
    }

    public void setHonors(String honors) {
        this.honors = honors;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Ceremony getCeremony() {
        return ceremony;
    }

    public void setCeremony(Ceremony ceremony) {
        this.ceremony = ceremony;
    }
}
