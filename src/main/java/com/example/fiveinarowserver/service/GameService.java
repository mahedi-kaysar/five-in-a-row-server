package com.example.fiveinarowserver.service;

import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.game.ConnectionInfo;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.entity.Game;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface GameService {

    /**
     * This method allows to register a player in the game till it's maximum allowed player limit (default 2)
     * and after adding a player if the server reaches to it's max limit a game will be instantiated and the
     * first player will get the next turn and get an available color (PLAYER_O or PLAYER_X).
     *
     * @param playerDto
     * @return ConnectionInfo
     * @Throws GameException if max allowed player limit (Default 2) exceeded.
     */
    ConnectionInfo registerPlayer(final PlayerDto playerDto);

    /**
     * This method remove a player from the server and stop a game if exist.
     * @param playerId
     * @throws GameException if player not exist to delete.
     */
    void disconnectPlayer(final int playerId);

    /**
     * This method get the current state of the game if exit.
     * @return Game
     * @throws GameException if game not exit/started.
     */
    Game getCurrentGameState();

    /**
     * The method update the board of the first game with the give column number and playerId.
     *
     * @param playerId
     * @param column
     * @return GameException if wrong player's turn, wrong column and game not exit.
     */
    Game updateBoardAndAlterNextTurn(final int playerId, final int column);
}
