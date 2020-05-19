# GradCraft
Graduation run-of-show plugin for Quaranteen University 2020

use this for resetting timeslots:
`UPDATE graduates SET timeslot = FROM_UNIXTIME(30 * id + UNIX_TIMESTAMP()), graduated = false WHERE 1=1;`
