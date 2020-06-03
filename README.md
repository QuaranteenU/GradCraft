# GradCraft
Graduation run-of-show plugin for Quaranteen University, which allows Minecraft servers to run automated graduation ceremonies. This plugin enables the usage of the following commands:

- `/diploma`: Runs a SQL query to check if the current player's UUID has a record in the table of graduates. If so, it gives the player a diploma (a custom filled map) with their name and information. If the user already has a filled map or isn't a graduate, they will be notified.
- `/robes <red|black|blue>`: If the current user is a graduate (checked same way as above), it will give them robes (armor). For this to appear correctly, you'll need the [GradPack](https://github.com/QuaranteenU/GradPack) texture pack.
- `/cerlist`: Displays a list of ceremonies (name and id) stored in SQL.
- `/cerstart <id>`: Starts the ceremony with the corresponding id. The user who runs this command will be marked as the showrunner, and will see extra information in their chat window on whether the ceremony is running on time.
- `/cerstop`: Stops the currently running ceremony.
- `/cerstatus`: Shows the status of the currently running ceremony.
- `/cernext`: Skips to the next graduate in the currently running ceremony.
- `/cerclaim`: Sets the user who runs this command as the currently running ceremony's showrunner, who can then run commands like `/cerstatus` and `/cerstop`, etc.

*Make sure that permissions for `/cer` commands are restricted to admins to prevent users from interfering with the ceremony!*

## Installation
After cloning this repository, do a gradle build (we recommend using IntelliJ IDEA). If it's successful, inside the `build/libs` directory you'll see `GradCraft-1.0-SNAPSHOT-all.jar`. Place that jar in your server plugins folder and restart your server. Once the plugin has loaded, you'll notice a GradCraft folder with a `config.yml` inside. You'll need to edit that file to work for your specific setup.

- First you'll need to update the the SQL database connection information. Make sure that the server URL includes a timezone parameter that reflects where the showrunners are, e.g. if the server's timezone is PST and showrunners are in EST, you'll want to append `?serverTimezone=America/New_York&useLegacyDatetimeCode=false` to your server URL so that the graduates' timeslots are localized correctly. This is because SQL inserts timestamps as UTC but converts them to the clients timezone when reading.
- Next, set the name of the world and stage region (created by an admin with WorldGuard). This is so that the ceremony only runs on the specified world (so if you're on a server with multiple worlds the ceremony won't interfere with them), and the stage region prevents users from crowding the stage when it's not their turn.
- Finally, you have to set some coordinates. `gradTpPoint` is where graduates will be teleported to when it's their turn (a good location will be the side of your stage), and `gradTpOutPoint` will be where they're sent when their time is up (e.g. off stage). `gradCenterPoint` is where a Graduate NPC (if the actual graduate isn't there/doesn't have Minecraft) will walk to and pause (facing `gradFacePoint`) before walking to `gradEndPoint` and despawning. Finally, `degreeNpcPoint` is where Professor Steve will spawn (graduates can right-click Professor Steve to receive their diploma, the same way the `/diploma` command works).

After that, the plugin is all setup! The only thing left todo is load your data into your SQL database using the script from our [csv-scripts](https://github.com/QuaranteenU/csv-scripts) repo.

## Running the ceremony
By itself, this plugin just handles the teleporting of grads/creation of NPCS, handing out diplomas/robes, and displaying graduates names on screen. Since Minecraft doesn't have a performant, built-in way of broadcasting audio, and since we were going to stream this over Twitch anyways, we created a script in our [csv-scripts](https://github.com/QuaranteenU/csv-scripts) repo to play audio (the reading of peoples' names and their senior quotes). If you're going to use that script, make sure you run it **BEFORE** running `/cerstart`. This is because the audio playing script queries the database for all the graduates who haven't yet graduated (i.e. their `graduated` flag is false) and schedules their audio clips. However, `/cerstart` marks graduates as `graduated` as soon as they're on-deck (so the first graduate will be set as `graduated` immediately upon running `/cerstart`).

## In case things go wrong
Sometimes things break, be it due to server load or a plugin conflict, etc. This may cause the ceremony to get disrupted. However, there's an easy way to get the ceremony back on track after you fix the problem. If issues are happening, run `/cerstop` and fix them (or if the server crashes you don't have to bother with `/cerstop`). When you're ready, run the following SQL query on your database:

```sql
SET @row_number = 0; 
UPDATE graduates SET timeslot = FROM_UNIXTIME(30 * (@row_number:=@row_number + 1) + UNIX_TIMESTAMP()) WHERE graduated=0;
```

This will update the timeslots of all graduates who haven't yet graduated by starting 30 seconds from the moment the query is run and going from there. So after running this query, you can run the ceremony audio script (if you're using it) and run `/cerstart` again and the ceremony will resume shortly after.