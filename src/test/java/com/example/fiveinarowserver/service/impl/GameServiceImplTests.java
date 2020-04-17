package com.example.fiveinarowserver.service.impl;

import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.model.board.BoardDiscColor;
import com.example.fiveinarowserver.model.game.ConnectionInfo;
import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.model.player.PlayerDto;
import com.example.fiveinarowserver.repository.GameRepository;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import com.example.fiveinarowserver.service.PlayerService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTests {

    @Mock
    GameRepository gameRepository;

    @Mock
    PlayerService playerService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void registerPlayer_GameDoesNoStart() {
        PlayerDto playerDto = PlayerDto.builder().name("testName1").build();
        Player player = Player.builder().id(1).name(playerDto.getName())
                .color(BoardDiscColor.PLAYER_O).build();
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        when(playerService.addNewPlayer(any())).thenReturn(player);
        when(playerService.getAllPlayers()).thenReturn(players);
        when(playerService.getMaxAllowedPlayers()).thenReturn(2);
        ConnectionInfo connectionInfo = gameService.registerPlayer(playerDto);
        Assertions.assertThat(connectionInfo.getPlayer().getId()).isEqualTo(1);
        Assertions.assertThat(connectionInfo.getGame()).isEqualTo(null);
    }

    @Test
    public void registerPlayer_GameStarts() {
        PlayerDto playerDto = PlayerDto.builder().build();
        Player playerOne = Player.builder().id(1).name("testName1")
                .color(BoardDiscColor.PLAYER_O).build();
        Player playerTwo = Player.builder().id(2).name("testName2")
                .color(BoardDiscColor.PLAYER_X).build();
        ArrayList<Player> players = new ArrayList<>();
        players.add(playerOne);
        players.add(playerTwo);
        when(playerService.addNewPlayer(any())).thenReturn(playerTwo);
        when(playerService.getAllPlayers()).thenReturn(players);
        when(playerService.getMaxAllowedPlayers()).thenReturn(2);
        Game game = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(Board.builder().build()).playerToNextTurn(playerOne).build();
        when(gameRepository.initializeGame(playerOne)).thenReturn(game);
        ConnectionInfo connectionInfo = gameService.registerPlayer(playerDto);
        Assertions.assertThat(connectionInfo.getPlayer().getId()).isEqualTo(2);
        Assertions.assertThat(connectionInfo.getGame().getId()).isEqualTo(1);
    }

    @Test(expected = GameException.class)
    public void registerPlayer_UnSucceed() throws Exception {
        PlayerDto playerDto = PlayerDto.builder().build();
        when(playerService.addNewPlayer(any())).thenThrow(GameException.class);

        gameService.registerPlayer(playerDto);
    }

    @Test
    public void disconnectPlayer_StopsGameIfRunning(){
        Game game = Game.builder().id(1).gameStatus(GameStatus.INITIALISED).build();
        when(gameRepository.getGameOne()).thenReturn(Optional.of(game));
        gameService.disconnectPlayer(2);
        verify(gameRepository, times(1)).removeGame(game);
    }

    @Test
    public void disconnectPlayer_WhensGameNotExist() {
        when(gameRepository.getGameOne()).thenReturn(Optional.empty());
        gameService.disconnectPlayer(1);
        verify(gameRepository, times(0)).removeGame(any());
    }

    @Test(expected = GameException.class)
    public void disconnectPlayer_WhensPlayerNotExit() throws Exception {
        doThrow(GameException.class).when(playerService).removePlayer(1);
        gameService.disconnectPlayer(1);
    }

    @Test
    public void updateBoardAndAlterNextTurn_validColumnAndValidPlayer() {
        Player playerOne = Player.builder().id(1).name("testName1")
                .color(BoardDiscColor.PLAYER_O).build();
        Player playerTwo = Player.builder().id(2).name("testName2")
                .color(BoardDiscColor.PLAYER_X).build();
        ArrayList<Player> players = new ArrayList<>();
        players.add(playerOne);
        players.add(playerTwo);
        when(playerService.getAllPlayers()).thenReturn(players);
        Board board = new Board(6, 9);
        Game game = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(board)
                .playerToNextTurn(playerOne).build();
        when(gameRepository.getGameOne()).thenReturn(Optional.of(game));
        when(playerService.findPlayer(anyInt())).thenReturn(playerOne);
        Board updatedBoard = new Board(6, 9);
        updatedBoard.getTable()[6][9] = BoardDiscColor.PLAYER_O;
        Game updatedGame = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(updatedBoard)
                .playerToNextTurn(playerOne).build();
        when(gameRepository.updateBoard(anyInt(), any(), anyInt())).thenReturn(updatedGame);

        Game result = gameService.updateBoardAndAlterNextTurn(1, 9);

        Assertions.assertThat(result.getPlayerToNextTurn()).isEqualTo(playerTwo);
    }

    @Test(expected = GameException.class)
    public void updateBoardAndAlterNextTurn_InvalidColumnAndValidPlayer() throws Exception {
        Player playerOne = Player.builder().id(1).name("testName1")
                .color(BoardDiscColor.PLAYER_O).build();
        Player playerTwo = Player.builder().id(2).name("testName2")
                .color(BoardDiscColor.PLAYER_X).build();
        Board board = new Board(6, 9);
        Game game = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(board)
                .playerToNextTurn(playerOne).build();
        when(gameRepository.getGameOne()).thenReturn(Optional.of(game));
        when(playerService.findPlayer(anyInt())).thenReturn(playerOne);
        Board updatedBoard = new Board(6, 9);
        updatedBoard.getTable()[6][9] = BoardDiscColor.PLAYER_O;
        Game updatedGame = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(updatedBoard)
                .playerToNextTurn(playerOne).build();
        when(gameRepository.updateBoard(anyInt(), any(), anyInt())).thenThrow(GameException.class);

        Game result = gameService.updateBoardAndAlterNextTurn(1, 10);
    }

    @Test(expected = GameException.class)
    public void updateBoardAndAlterNextTurn_InvalidPlayer() throws Exception {
        Player playerOne = Player.builder().id(1).name("testName1")
                .color(BoardDiscColor.PLAYER_O).build();
        Player playerTwo = Player.builder().id(2).name("testName2")
                .color(BoardDiscColor.PLAYER_X).build();
        Board board = new Board(6, 9);
        Game game = Game.builder().id(1).gameStatus(GameStatus.INITIALISED)
                .board(board)
                .playerToNextTurn(playerOne).build();
        when(gameRepository.getGameOne()).thenReturn(Optional.of(game));
        when(playerService.findPlayer(anyInt())).thenReturn(playerTwo);
        gameService.updateBoardAndAlterNextTurn(1, 9);
    }
}