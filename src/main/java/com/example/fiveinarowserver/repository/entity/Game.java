package com.example.fiveinarowserver.repository.entity;

import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.model.game.GameStatus;
import lombok.*;

@Data
@Builder
public class Game {
    private final int id;

    private final Board board;

    private GameStatus gameStatus;

    private Player playerToNextTurn;
}