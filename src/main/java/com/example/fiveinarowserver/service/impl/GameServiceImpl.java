package com.example.fiveinarowserver.service.impl;

import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.game.ConnectionInfo;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.GameRepository;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import com.example.fiveinarowserver.service.GameService;
import com.example.fiveinarowserver.service.PlayerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Log4j2
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerService playerService;

    /**
     *
     * @param playerDto
     * @return
     */
    public ConnectionInfo registerPlayer(PlayerDto playerDto) {
        Player player = this.playerService.addNewPlayer(playerDto);

        ArrayList<Player> players = this.playerService.getAllPlayers();
        if (players.size() == this.playerService.getMaxAllowedPlayers()) {
            Game game = this.gameRepository.initializeGame();
            this.gameRepository.updatePlayerToNextTurn(game.getId(), players.get(0));
            log.info("The game has been started ...");
        }

        Optional<Game> game = this.gameRepository.getGameOne();
        if (game.isPresent()) {
            return  ConnectionInfo.builder().player(player).game(game.get()).build();
        } else {
            return ConnectionInfo.builder().player(player).build();
        }
    }

    /**
     *
     * @param playerId
     */
    @Override
    public void disconnectPlayer(int playerId) {
        this.playerService.removePlayer(playerId);
        stopGameIfExit();
    }

    /**
     *
     * @return
     */
    public Game getCurrentGameState() {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if (optional.isPresent()) {
            return this.gameRepository.getGameOne().get();
        }
        throw new GameException("Game has not been started", HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param playerId
     * @param column
     * @return
     */
    public Game updateBoardAndAlterNextTurn(int playerId, int column) {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if(optional.isPresent()) {
            Game game = optional.get();
            if (isValidPlayerToTurn(game, playerId)) {
                Player player = playerService.findPlayer(playerId);
                game = this.gameRepository.updateBoard(game.getId(), player, column);
                try {
                    updateNextTurn(game);
                } catch (Exception e) {
                    log.error("And error occurred during next turn assignment", e);
                }
                return game;
            } else {
                throw new GameException("This is not your turn", HttpStatus.BAD_REQUEST);
            }
        }
        throw new GameException("Game has not been started", HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param game
     */
    private void updateNextTurn(Game game) {
        Player nextPlayer = findNextPlayerToTurn(game);
        game.setPlayerToNextTurn(nextPlayer);
    }

    /**
     *
     * @param game
     * @param playerId
     * @returndisconnectPlayer
     */
    private boolean isValidPlayerToTurn(Game game, int playerId) {
        Player player = playerService.findPlayer(playerId);
        return player.getId() == game.getPlayerToNextTurn().getId();
    }

    /**
     *
     * @param game
     * @return
     */
    private Player findNextPlayerToTurn(Game game) {
        ArrayList<Player> players = this.playerService.getAllPlayers();
        for (Player player: players) {
            if (player.getId() != game.getPlayerToNextTurn().getId()) {
                return player;
            }
        }
        throw new GameException("Player not found to assign next turn");
    }

    private void stopGameIfExit() {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if (optional.isPresent()) {
            this.gameRepository.removeGame(optional.get());
            log.info("Game is stopped");
        }
    }
}

