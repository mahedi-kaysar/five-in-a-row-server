package com.example.fiveinarowserver.repository;

import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.model.board.BoardDiscColor;
import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameRepositoryTest {
    @InjectMocks
    GameRepository gameRepository;

    @Test
    public void didWin_checkVerticalWin() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_O).build();
        Board board = new Board(6,9);
        board.getTable()[6][1] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][1] = BoardDiscColor.PLAYER_O;
        board.getTable()[4][1] = BoardDiscColor.PLAYER_O;
        board.getTable()[3][1] = BoardDiscColor.PLAYER_O;
        board.getTable()[2][1] = BoardDiscColor.PLAYER_O;
        board.getTable()[1][1] = BoardDiscColor.PLAYER_O;


        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.didWin(game);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void didWin_checkHorizontalWin() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_O).build();
        Board board = new Board(6,9);
        board.getTable()[6][1] = BoardDiscColor.PLAYER_X;
        board.getTable()[6][2] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][3] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][4] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][5] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][6] = BoardDiscColor.PLAYER_O;

        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.didWin(game);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void didWin_checkDiagonalWin_success() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_O).build();
        Board board = new Board(6,9);
        board.getTable()[6][4] = BoardDiscColor.PLAYER_O; board.getTable()[5][5] = BoardDiscColor.PLAYER_O;
        board.getTable()[4][6] = BoardDiscColor.PLAYER_O; board.getTable()[3][7] = BoardDiscColor.PLAYER_O;
        board.getTable()[2][8] = BoardDiscColor.PLAYER_O; board.getTable()[5][4] = BoardDiscColor.PLAYER_O;
        board.getTable()[5][3] = BoardDiscColor.PLAYER_O; board.getTable()[5][2] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][7] = BoardDiscColor.PLAYER_O; board.getTable()[3][8] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][5] = BoardDiscColor.PLAYER_X; board.getTable()[6][6] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][6] = BoardDiscColor.PLAYER_X; board.getTable()[3][6] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][7] = BoardDiscColor.PLAYER_X; board.getTable()[4][7] = BoardDiscColor.PLAYER_X;
        board.getTable()[2][7] = BoardDiscColor.PLAYER_X; board.getTable()[6][8] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][8] = BoardDiscColor.PLAYER_X; board.getTable()[4][8] = BoardDiscColor.PLAYER_X;

        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.didWin(game);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void didWin_checkDiagonalWin_unSuccess() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_X).build();
        Board board = new Board(6,9);
        board.getTable()[6][4] = BoardDiscColor.PLAYER_O; board.getTable()[5][5] = BoardDiscColor.PLAYER_O;
        board.getTable()[4][6] = BoardDiscColor.PLAYER_O; board.getTable()[3][7] = BoardDiscColor.PLAYER_O;
        board.getTable()[2][8] = BoardDiscColor.PLAYER_O; board.getTable()[5][4] = BoardDiscColor.PLAYER_O;
        board.getTable()[5][3] = BoardDiscColor.PLAYER_O; board.getTable()[5][2] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][7] = BoardDiscColor.PLAYER_O; board.getTable()[3][8] = BoardDiscColor.PLAYER_O;
        board.getTable()[6][5] = BoardDiscColor.PLAYER_X; board.getTable()[6][6] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][6] = BoardDiscColor.PLAYER_X; board.getTable()[3][6] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][7] = BoardDiscColor.PLAYER_X; board.getTable()[4][7] = BoardDiscColor.PLAYER_X;
        board.getTable()[2][7] = BoardDiscColor.PLAYER_X; board.getTable()[6][8] = BoardDiscColor.PLAYER_X;
        board.getTable()[5][8] = BoardDiscColor.PLAYER_X; board.getTable()[4][8] = BoardDiscColor.PLAYER_X;

        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.didWin(game);
        Assertions.assertThat(result).isEqualTo(false);
    }

    @Test
    public void isMatchTied_true() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_O).build();
        Board board = new Board(2,2);
        board.getTable()[2][1] = BoardDiscColor.PLAYER_X; board.getTable()[2][2] = BoardDiscColor.PLAYER_O;
        board.getTable()[1][1] = BoardDiscColor.PLAYER_O; board.getTable()[1][2] = BoardDiscColor.PLAYER_X;

        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.isMatchTied(game);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void isMatchTied_false() {
        Player player = Player.builder().id(1).name("testName").color(BoardDiscColor.PLAYER_O).build();
        Board board = new Board(2,2);
        board.getTable()[2][1] = BoardDiscColor.PLAYER_X; board.getTable()[2][2] = BoardDiscColor.PLAYER_O;
        board.getTable()[1][1] = BoardDiscColor.PLAYER_O;

        Game game = Game.builder().id(1).playerToNextTurn(player)
                .board(board).gameStatus(GameStatus.INITIALISED).build();
        boolean result = gameRepository.isMatchTied(game);
        Assertions.assertThat(result).isEqualTo(false);
    }
}