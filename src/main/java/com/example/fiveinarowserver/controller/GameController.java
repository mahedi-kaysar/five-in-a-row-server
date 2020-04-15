package com.example.fiveinarowserver.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController(value = "game/")
@Log4j2
public class GameController {
    @GetMapping(path = "/{id}")
    public Boolean getGameStatus(@RequestParam Integer id){
        log.info("Game ID=" + id);
        return true;
    }
}
