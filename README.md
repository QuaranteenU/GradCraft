# GradCraft

Graduation run-of-show plugin for Quaranteen University 2020

use this for resetting timeslots:
```sql
SET @row_number = 0; 
UPDATE graduates SET timeslot = FROM_UNIXTIME(30 * (@row_number:=@row_number + 1) + UNIX_TIMESTAMP()) WHERE ceremony=24 AND graduated=0;
```

`op @p[name="NAME HERE"]`
