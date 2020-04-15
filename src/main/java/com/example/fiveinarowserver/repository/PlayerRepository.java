package com.example.fiveinarowserver.repository;

import com.example.fiveinarowserver.repository.entity.Player;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;

@Repository
@Data
public class PlayerRepository {
    private ArrayList<Player> players;
    private final char[] availableColors = {'O', 'X'};
    private HashSet<Character> usedColorSet;
    @Value("${game.maxAllowedPlayers:2}")
    private int maxAllowedPlayers;
    private static PlayerRepository playerRepository;

    public PlayerRepository() {
        this.players = new ArrayList<>();
        usedColorSet = new HashSet<>();
    }
    public static PlayerRepository getInstance() {
        if (playerRepository == null) {
            playerRepository = new PlayerRepository();
        }
        return playerRepository;
    }

    public Player addNewPlayer(final Player player) {
        if (this.maxAllowedPlayers <= this.players.size()){
            throw new RuntimeException(
              String.format("Allowed (%s) number of players already found", this.maxAllowedPlayers)
            );
        }
        player.setId(this.players.size() + 1);
        assignAvailableColor(player);
        players.add(player);
        return player;
    }

    public ArrayList<Player> getAllPlayers() {
        return this.players;
    }

    private void assignAvailableColor(Player player) {
        if (usedColorSet.contains(availableColors[0])) {
            player.setColor(availableColors[1]);
            usedColorSet.add(availableColors[1]);
        } else {
            player.setColor(availableColors[0]);
            usedColorSet.add(availableColors[0]);
        }
    }
}
