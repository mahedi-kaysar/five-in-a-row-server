package com.example.fiveinarowserver.service;

import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.repository.GameRepository;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerService playerService;

    public Game start() {
        return this.gameRepository.initializeGame();
    }

    public Game getCurrentGameState() {
        Optional<Game> optional = this.gameRepository.getGameOne();
        if (optional.isPresent()) {
            return this.gameRepository.getGameOne().get();
        }
        throw new RuntimeException("Game has not been started");
    }

    public Optional<Game> getGameOne() {
        return this.gameRepository.getGameOne();
    }

    public Game updatePlayerToNextTurn(int gameId, Player player) {
        return this.gameRepository.updatePlayerToNextTurn(gameId, player).get();
    }

    public Game updateBoardAndAlterNextTurn(int playerId, int column) {
        Optional<Game> optional = getGameOne();
        if(optional.isPresent()) {
            Game game = optional.get();
            if (isValidPlayerToTurn(game, playerId)) {
                Player player = playerService.findPlayer(playerId);
                game = this.gameRepository.updateBoard(game.getId(), player, column);
                updateNextTurn(game);
                return game;
            } else {
                throw new RuntimeException("Invalid Player to next turn");
            }
        }
        throw new RuntimeException("Game not exist.");
    }

    private void updateNextTurn(Game game) {
        Player nextPlayer = findNextPlayerToTurn(game);
        game.setPlayerToNextTurn(nextPlayer);
    }

    private boolean isValidPlayerToTurn(Game game, int playerId) {
        Player player = playerService.findPlayer(playerId);
        return player.getId() == game.getPlayerToNextTurn().getId();
    }

    private Player findNextPlayerToTurn(Game game){
        ArrayList<Player> players = this.playerService.getAllPlayers();
        for (Player player: players) {
            if (player.getId() != game.getPlayerToNextTurn().getId()) {
                return player;
            }
        }
        throw new RuntimeException("Players not found.");
    }
}
