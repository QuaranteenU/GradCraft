# GradCraft
Graduation run-of-show plugin for Quaranteen University 2020

use this for resetting timeslots:
`UPDATE graduates SET timeslot = FROM_UNIXTIME(30 * @rownum + UNIX_TIMESTAMP()), graduated = false WHERE 1=1;`
`op @p[name="NAME HERE"]`
