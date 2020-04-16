package com.example.fiveinarowserver.service.unit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.fiveinarowserver.model.game.ConnectionInfo;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.GameRepository;
import com.example.fiveinarowserver.service.PlayerService;
import com.example.fiveinarowserver.service.impl.GameServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    PlayerService playerService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void registerPlayer() {
        PlayerDto playerDto = PlayerDto.builder().name("testName1").build();
        ConnectionInfo connectionInfo = gameService.registerPlayer(playerDto);
        Assertions.assertThat(connectionInfo.getPlayer().getId()).isEqualTo(1);
    }
}