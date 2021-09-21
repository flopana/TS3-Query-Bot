# Contributing to flopana/ts3-query-bot
If you want to contribute to this repository simply pick one of the // TODO: comments scattered around the code
base or Pick one of the new planned features and try to implement it.

## Planned features
```
!msgchannelgroup <cgroup id> <message>
!msgservergroup <sgroup id> <message>

!pokeall <message>
!pokechannelgroup <cgroup id> <message>
!pokeservergroup <sgroup id> <message>

!searchnickname <regex> ✓ Implemented
!searchip <ip> - search for an ip in the ts3 database
!searchdbid <db_id> - search for a db ip in the ts3 database
```

## Known issue that need to be fixed
- Answers from the bot can exceed the message lenght limit and fail (need to be split into multiple messages)