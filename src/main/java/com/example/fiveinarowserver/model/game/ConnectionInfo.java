package com.example.fiveinarowserver.model.game;

import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionInfo {
    private Player player;
    private Game game;
}