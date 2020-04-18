# Five-in-a-row game server
## Rules and assumptions
1) upon start, server waits for the two players to connect. 
2) More than two players can't connect in that server, however, limit is configurable.
3) If one of the players disconnects, the game is over and all players will be removed.
4) More that 1 game can't be started, however, limit is configurable.
5) Server will decide the color of a corresponding player (PLAYER_O or PLAYER_X). Default color of the board is (NONE).
6) For each turn, server will check if a player is won or game is tied.

## Rest Endpoints summary:
Register Player:
```$xslt
curl -H "Content-Type: application/json" -X POST http://localhost:8080/game/register/player -d '{"name":"Mahedi"}'
curl -H "Content-Type: application/json" -X POST http://localhost:8080/game/register/player -d '{"name":"John"}'
```
Get Game state and player to next turn:
```$xslt
curl -X GET http://localhost:8080/game/board/state
```
Play next move:
```$xslt
curl -X GET http://localhost:8080/game/player/<playerId>/next-move/<columnToMove>
curl -X GET http://localhost:8080/game/player/1/next-move/8
```
Disconnect Player:
```$xslt
curl -X DELETE http://localhost:8080/game/player/<playerId>/disconnect
curl -X DELETE http://localhost:8080/game/player/1/disconnect
```
## Required software
* java 8+
* mvn 3.6+
## how to test and run
```$xslt
mvn clean test
mvn spring-boot:run
```
## Test Coverage:
```$xslt
mvn test
cat target/site/jacoco/index.html
```
## server: http://localhost:8080

## JAVA client: https://github.com/mahedi-kaysar/five-in-a-row-client
Please use this client for playing the game.

## Future Work
* Add more Unit test
* Add Integration test
* Improve java docs
* Improve design
* Add more feature.