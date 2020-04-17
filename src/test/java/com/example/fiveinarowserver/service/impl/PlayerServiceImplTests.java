package com.example.fiveinarowserver.service.impl;

import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.PlayerRepository;
import com.example.fiveinarowserver.repository.entity.Player;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceImplTests {
    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerServiceImpl playerService;

    @Test
    public void addNewPlayer_withExistingNoPlayer() {
        PlayerDto playerDto = PlayerDto.builder().name("testName").build();
        Player player = Player.builder().id(1).name(playerDto.getName()).build();
        when(playerRepository.addNewPlayer(any())).thenReturn(player);
        Player result = playerService.addNewPlayer(playerDto);
        Assertions.assertThat(result.getId()).isEqualTo(1);
    }

    @Test(expected = GameException.class)
    public void addNewPlayer_withPlayerLimitExceeded() {
        PlayerDto playerDto = PlayerDto.builder().name("testName").build();
        when(playerRepository.addNewPlayer(any())).thenThrow(GameException.class);
        playerService.addNewPlayer(playerDto);
    }
}