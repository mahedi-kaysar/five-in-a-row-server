package com.example.fiveinarowserver.service;

import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.entity.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface PlayerService {
    /**
     *
     * @param playerDto
     * @return
     */
    public Player addNewPlayer(PlayerDto playerDto);

    /**
     *
     * @return
     */
    public ArrayList<Player> getAllPlayers();

    /**
     *
     * @return
     */
    public int getMaxAllowedPlayers();

    /**
     *
     * @param playerId
     * @return
     */
    public Player findPlayer(int playerId);

    /**
     *
     * @param playerId
     */
    void removePlayer(int playerId);

    /**
     *
     */
    void removeAllPlayers();
}
