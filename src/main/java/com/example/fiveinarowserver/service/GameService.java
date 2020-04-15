package com.example.fiveinarowserver.service;

import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.repository.GameRepository;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
