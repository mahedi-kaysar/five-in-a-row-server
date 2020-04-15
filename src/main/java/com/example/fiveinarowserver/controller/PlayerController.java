package com.example.fiveinarowserver.controller;

import com.example.fiveinarowserver.model.game.ConnectionInfo;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import com.example.fiveinarowserver.service.GameService;
import com.example.fiveinarowserver.service.PlayerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "players", consumes = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class PlayerController {

    @Autowired
    PlayerService playerService;
    @Autowired
    GameService gameService;

    @PostMapping(path = "/connect", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionInfo registerPlayer(@RequestBody PlayerDto playerDto) {
        Player player = playerService.addNewPlayer(playerDto);

        ArrayList<Player> players = playerService.getAllPlayers();
        if (players.size() == playerService.getMaxAllowedPlayers()) {
            Game game = gameService.start();
            gameService.updatePlayerToNextTurn(game.getId(), players.get(0));
            log.info("The game has been started ...");
        }

        Optional<Game> game = gameService.getGameOne();
        if (game.isPresent()) {
            return  ConnectionInfo.builder().player(player).game(game.get()).build();
        } else {
            return ConnectionInfo.builder().player(player).build();
        }
    }
}