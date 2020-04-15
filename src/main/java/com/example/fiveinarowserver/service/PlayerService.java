package com.example.fiveinarowserver.service;

import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.PlayerRepository;
import com.example.fiveinarowserver.repository.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public Player addNewPlayer(PlayerDto playerDto) {
        Player player = Player.builder().name(playerDto.getName()).build();
        return this.playerRepository.addNewPlayer(player);
    }
    public ArrayList<Player> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    public int getMaxAllowedPlayers() {
        return playerRepository.getMaxAllowedPlayers();
    }
}
