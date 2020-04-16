package com.example.fiveinarowserver.config;

import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.service.GameService;
import com.example.fiveinarowserver.service.PlayerService;
import com.example.fiveinarowserver.service.impl.GameServiceImpl;
import com.example.fiveinarowserver.service.impl.PlayerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GameService gameService() {
        return new GameServiceImpl();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerServiceImpl();
    }
}
