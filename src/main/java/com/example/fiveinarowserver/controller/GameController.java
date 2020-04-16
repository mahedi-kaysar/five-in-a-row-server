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

@Log4j2
@RestController
@RequestMapping(path = "game")
public class GameController {
    /**
     * An instance of PlayerService
     */
    @Autowired
    PlayerService playerService;

    /**
     * An instance of GameService
     */
    @Autowired
    GameService gameService;

    /**
     * This endpoint receives PlayerDto as request body and register that player in the game.
     *
     * @param playerDto
     * @return
     */
    @PostMapping(path = "/register/player", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionInfo registerPlayer(@RequestBody PlayerDto playerDto) {
        return  gameService.registerPlayer(playerDto);
    }

    @DeleteMapping(path = "/player/{playerId}/disconnect")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disconnectPlayer(@PathVariable int playerId) {
        this.gameService.disconnectPlayer(playerId);
    }

    @GetMapping(path = "/board/state")
    public Game getBoardStatusAndTurn() {
        return gameService.getCurrentGameState();
    }

    @GetMapping(path = "/player/{playerId}/next-move/{column}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Game executeNextMove(@PathVariable int playerId, @PathVariable int column) {
        return gameService.updateBoardAndAlterNextTurn(playerId, column);
    }
}
