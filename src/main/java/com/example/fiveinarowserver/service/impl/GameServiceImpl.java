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

/**
 * This is a GameService.
 */
@Log4j2
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerService playerService;

    /**
     * This method allows to register a player in the game till it's maximum allowed player limit (default 2)
     * and after adding a player if the server reaches to it's max limit a game will be instantiated and the
     * first player will get the next turn and get an available color (PLAYER_O or PLAYER_X).
     *
     * @param playerDto
     * @return ConnectionInfo
     * @Throws GameException if max allowed player limit (Default 2) exceeded.
     */
    public ConnectionInfo registerPlayer(final PlayerDto playerDto) {
        Player player = this.playerService.addNewPlayer(playerDto);
        ArrayList<Player> players = this.playerService.getAllPlayers();
        if (players.size() == this.playerService.getMaxAllowedPlayers()) {
            Game game = this.gameRepository.initializeGame(players.get(0));
            log.info("The game has been started ...");
            return  ConnectionInfo.builder().player(player).game(game).build();
        }
        return ConnectionInfo.builder().player(player).build();
    }

    /**
     * This method remove a player from the server and stop a game if exist.
     * @param playerId
     * @throws GameException if player not exist to delete.
     */
    @Override
    public void disconnectPlayer(final int playerId) {
        this.playerService.removePlayer(playerId);
        stopGameIfExit();
    }

    /**
     * This method get the current state of the game if exit.
     * @return Game
     * @throws GameException if game not exit/started.
     */
    public Game getCurrentGameState() {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if (optional.isPresent()) {
            return this.gameRepository.getGameOne().get();
        }
        throw new GameException("Game has not been started", HttpStatus.NOT_FOUND);
    }

    /**
     * The method update the board of the first game with the give column number and playerId.
     *
     * @param playerId
     * @param column
     * @return GameException if wrong player's turn, wrong column and game not exit.
     */
    public Game updateBoardAndAlterNextTurn(final int playerId, final int column) {
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

    /**
     *
     */
    private void stopGameIfExit() {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if (optional.isPresent()) {
            this.gameRepository.removeGame(optional.get());
            log.info("Game is stopped");
            this.playerService.removeAllPlayers();
        }
    }
}

