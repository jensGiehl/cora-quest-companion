# CoraQuest Progress to BGG

A small web application for tracking your campaign progress in the board game [CoraQuest](https://boardgamegeek.com/boardgame/341828/coraquest) and exporting it as a formatted comment for your play entry on [BoardGameGeek](https://boardgamegeek.com).

## What it does

CoraQuest is a cooperative dungeon-crawling board game with a campaign mode. After each session you log a "play" on BGG — but the standard play form gives you only a free-text comment field. This app gives you a structured form to record everything that matters about a session:

- Which **quest** you played and its **difficulty**
- Whether **Second Wind** was used
- Which **curses** your party carried
- How much **gold** remained
- Each **player's character**, remaining health, and the cards they held

Once filled in, the app generates a ready-to-paste BGG-formatted comment (using BGG's BBCode) that you can copy with one click and paste directly into the BGG play form.

Games are identified by a short 4-character code. Share the code with other players so everyone sees the same state in real time — every field is saved to the database immediately as you type.

## Documentation

- [Building, running & deploying](docs/running.md)
- [GitHub repository setup](docs/github-setup.md)
