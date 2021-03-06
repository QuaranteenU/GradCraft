# How to contribute
There's no strict guidelines, just try to follow the code style and folder structure that's already present in the repository. The team at QU has all used [IntelliJ IDEA](https://www.jetbrains.com/idea/) (which has a free student license and a community edition if you're not a student), so using IntelliJ will ensure that code style stays consistent for the most part. Just use your best judgement, at the end of the day any issues can be caught during PR review.

## Things you could potentially add
- Right now the way the graduate NPCs traverse the stage is pretty jank, it uses a navigation ended hook and boolean flags to decide what actions to take/where to move next. Making the navigation more easily configurable/robust would be very helpful!
- Fixing the nametag listener to change the names over the heads of graduates to be their real names instead of their Minecraft usernames.
- Automatically turn off weather cycle during ceremony (or custom weather patterns).
- Add command to show graduation time for user (ETA for when they'll be on stage/position in queue)
- Print senior quotes into chat along with other graduate information
- Change Paper Diploma Icon to Map Icon
