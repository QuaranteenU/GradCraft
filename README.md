# GradCraft
Graduation run-of-show plugin for Quaranteen University 2020

use this for resetting timeslots:
```
SET @row_number = 0; 
UPDATE graduates SET timeslot = FROM_UNIXTIME(30 * (@row_number:=@row_number + 1) + UNIX_TIMESTAMP()) WHERE 1=1;
```

`op @p[name="NAME HERE"]`
