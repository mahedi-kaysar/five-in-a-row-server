package com.example.fiveinarowserver.repository;

import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.model.board.BoardDiscColor;
import com.example.fiveinarowserver.repository.entity.Player;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Repository
@Data
public class PlayerRepository {
    private ArrayList<Player> players;
    private HashSet<BoardDiscColor> usedColorSet;
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
        if (usedColorSet.contains(BoardDiscColor.PLAYER_O)) {
            player.setColor(BoardDiscColor.PLAYER_X);
            usedColorSet.add(BoardDiscColor.PLAYER_X);
        } else {
            player.setColor(BoardDiscColor.PLAYER_O);
            usedColorSet.add(BoardDiscColor.PLAYER_O);
        }
    }

    public Optional<Player> findPlayer(final int playerId) {
        Optional<Player> optional = this.players.stream()
                .filter(player -> player.getId() == playerId)
                .findFirst();
        Optional<Player> player = Optional.empty();
        if(optional.isPresent()){
            player = Optional.of(optional.get());
        }
        return player;
    }

    public void removePlayer(final int playerId) {
        Optional<Player> optional = findPlayer(playerId);
        if (optional.isPresent()) {
            this.players.remove(optional.get());
        } else {
            throw new GameException("Player Id not found to remove", HttpStatus.NOT_FOUND);
        }
    }
}
