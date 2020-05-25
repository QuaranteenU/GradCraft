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

import university.quaranteen.gradcraft.GradCraftPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;


/**
 * This class is responsible for advancing the ceremony every 30 seconds
 */
public class CeremonyTimer implements Runnable {
    public CeremonyTimer(GradCraftPlugin plugin) {
        this.plugin = plugin;
    }

    public static final Duration MINIMUM_STAGE_TIME = Duration.ofSeconds(25);

    private final GradCraftPlugin plugin;
    private Graduate curGraduate;
    private Graduate nextGraduate;
    private Instant lastSwitch;
    private boolean initialized = false;
    private boolean running = false;
    private Instant startTime;

    @Override
    public void run() {
        Logger log = plugin.getLogger();
        if (plugin.ceremony == null) {
            curGraduate = null;
            nextGraduate = null;
            initialized = false;
            lastSwitch = Instant.now();
            return;
        }

        boolean gradChanged = false;

        if ((!initialized && curGraduate == null) || curGraduate != plugin.ceremony.getCurrentGraduate()) {
            if (curGraduate == null) {
                curGraduate = plugin.ceremony.getNextGraduate();
                plugin.ceremony.setCurrentGraduate(curGraduate);
                plugin.ceremony.getStageController().startCeremony(plugin.ceremony.getId(), plugin.ceremony.getShowRunner());
            } else
                curGraduate = plugin.ceremony.getCurrentGraduate();
            log.info("Initialized graduate w/ " + (curGraduate == null ? "null" : curGraduate.getName()));
            lastSwitch = Instant.now();
            if (curGraduate != null) {
                plugin.ceremony.signalGraduated(curGraduate);
                nextGraduate = plugin.ceremony.getNextGraduate();
                startTime = curGraduate.getTimeslot().toInstant();
                initialized = true;
                running = false;
                gradChanged = true;
            }
        }

        if (initialized && !running ) {
            log.info("Waiting for start in " + Duration.between(startTime, Instant.now()).getSeconds() + " second(s)");
            if (Instant.now().isAfter(startTime)) {
                running = true;
                gradChanged = true;
            }
        }

        if (running) {
            if (lastSwitch.plus(MINIMUM_STAGE_TIME).isBefore(Instant.now()) || curGraduate == null) {
                if (nextGraduate != null && Instant.now().isAfter(nextGraduate.getTimeslot().toInstant())) {
                    log.info("Switching graduates");

                    curGraduate = nextGraduate;

                    if (curGraduate != null) {
                        log.info("Signalling graduation for " + curGraduate.getName());
                        plugin.ceremony.signalGraduated(curGraduate);
                    }

                    nextGraduate = plugin.ceremony.getNextGraduate();
                    lastSwitch = Instant.now();

                    log.info("Current = " + (curGraduate == null ? "null" : curGraduate.getName()));
                    log.info("Next = " + (nextGraduate == null ? "null" : nextGraduate.getName()));

                    if (curGraduate != null) gradChanged = true;
                } else if (nextGraduate == null) {
                    gradChanged = false;
                    plugin.ceremony.getStageController().stopCeremony(plugin.ceremony.getId(), plugin.ceremony.getShowRunner());
                    plugin.ceremony = null;
                }
            }

            if (gradChanged) {
                StageController controller = plugin.ceremony.getStageController();
                controller.forceOutGraduate();
                plugin.ceremony.setCurrentGraduate(curGraduate);
                controller.teleportInGraduate();
                controller.notifyShowRunner(plugin.ceremony.getShowRunner());
                controller.notifySpectators();
            }
        }
    }
}
