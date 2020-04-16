package com.example.fiveinarowserver.service;

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
     *
     * @param playerDto
     * @return
     */
    ConnectionInfo registerPlayer(PlayerDto playerDto);

    /**
     * This method will disconnect a player and end the game at the same time.
     * @param playerId
     */
    void disconnectPlayer(int playerId);

    /**
     *
     * @param playerId
     * @param column
     * @return
     */
    Game updateBoardAndAlterNextTurn(int playerId, int column);

    /**
     *
     * @return
     */
    Game getCurrentGameState();
}
