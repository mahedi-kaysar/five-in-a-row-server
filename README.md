## Server
1) upon start, waits for the two players to connect. 
2) If one of the players disconnects, the game is over.

Rest Endpoints:
http://localhost:8080/player/connect
http://localhost:8080/player/<id>/disconnect
http://localhost:8080/game/start
http://localhost:8080/game/status
http://localhost:8080/board/status
http://localhost:8080/board/nextMove

## Client
1) upon start, the client prompts the player to enter her name and displays whether it’s waiting for a 2nd player, or the game can start.
	each client will request to the bellow endpoint by providing his/her name.
	http://localhost:8080/player/connect -> payload{player:{name: mahedi, id=1, color='O'}, isGameInitilized=false}
	http://localhost:8080/player/connect -> payload{player:{name: Irin, id=2, color='X'}, isGameInitilized=true, Game: {id: 1}}
	(board will be initialized)
2) display current status of board and promot for next move if it's his turn
	http://localhost:8080/board/<id>/status --> payload{board: {hight, width, table}, isMyTurn: true}
	http://localhost:8080/board/<id>/status --> payload{board: {hight, width, table}, isMyTurn: false, isGameOver=false}
	if isGameOver=true then display gamestatus:
	http://localhost:8080/game/status -> payload{isGameCanceled: false, Winer: {name:Mahedi}}

3) take input from client of next move (1-9) and sumit
    http://localhost:8080/board/<id>/nextMove
	
4) http://localhost:8080/player/<id>/disconnect
