package university.quaranteen.gradcraft.model;

import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.List;

@Entity
public class Ceremony extends ModelBase {
    @NotNull
    private Instant startTime;

    @ManyToOne(targetEntity = Graduate.class)
    private List<Graduate> graduates;

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}
