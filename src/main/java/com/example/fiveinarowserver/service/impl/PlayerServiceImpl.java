package com.example.fiveinarowserver.service.impl;


import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.PlayerRepository;
import com.example.fiveinarowserver.repository.entity.Player;
import com.example.fiveinarowserver.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    /**
     *
     * @param playerDto
     * @return
     */
    @Override
    public Player addNewPlayer(PlayerDto playerDto) {
        Player player = Player.builder().name(playerDto.getName()).build();
        return this.playerRepository.addNewPlayer(player);
    }

    /**
     *
     * @return
     */
    @Override
    public ArrayList<Player> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    /**
     *
     * @return
     */
    @Override
    public int getMaxAllowedPlayers() {
        return playerRepository.getMaxAllowedPlayers();
    }

    /**
     *
     * @param playerId
     * @return
     */
    @Override
    public Player findPlayer(int playerId) {
        return this.playerRepository.findPlayer(playerId).get();
    }

    @Override
    public void removePlayer(int playerId) {
        this.playerRepository.removePlayer(playerId);
    }

    @Override
    public void removeAllPlayers() {
        this.playerRepository.removeAllPlayers();
        this.playerRepository.resetUserColor();
    }
}