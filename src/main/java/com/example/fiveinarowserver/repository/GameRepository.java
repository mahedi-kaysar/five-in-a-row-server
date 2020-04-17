package com.example.fiveinarowserver.repository;

import com.example.fiveinarowserver.model.board.BoardDiscColor;
import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 */
@Repository
public class GameRepository {

    /**
     *
     */
    private final ArrayList<Game> games;

    /**
     *
     */
    @Value("${game.maxSupportedGame:1}")
    private int maxSupportedGame;

    /**
     *
     */
    public GameRepository() {
        this.games = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public Optional<Game> getGameOne(){
        return this.games.stream().findFirst();
    }

    /**
     *
     * @return
     */
    public Game initializeGame(Player player) {
        if (this.maxSupportedGame  <= this.games.size())
            throw new RuntimeException(
                String.format("Allowed (%s) Number of Games have already been started=",
                this.maxSupportedGame)
            );

        Game game = Game.builder().id(this.games.size() + 1)
                .board(new Board(6, 9))
                .gameStatus(GameStatus.INITIALISED)
                .build();
        this.games.add(game);
        updatePlayerToNextTurn(game.getId(), player);
        return game;
    }

    /**
     *
     * @param gameId
     * @param gameStatus
     */
    public Optional<Game> updateGameStatus (final int gameId, final GameStatus gameStatus) {
        Optional<Game> optional = games.stream()
                .filter(game -> game.getId() == gameId)
                .findFirst();

        Optional<Game> game = Optional.empty();
        if (optional.isPresent()){
            Game updatedGame = optional.get();
            updatedGame.setGameStatus(gameStatus);
            game = Optional.of(updatedGame);
        }
        return game;
    }

    /**
     *
     * @param gameId
     * @return
     */
    public Optional<Board> getBoard(final int gameId) {
        Optional<Game> optional = games.stream()
                .filter(game -> game.getId() == gameId)
                .findFirst();
        Optional<Board> board = Optional.empty();
        if(optional.isPresent()){
            board = Optional.of(optional.get().getBoard());
        }
        return board;
    }

    /**
     *
     * @param gameId
     * @param player
     * @param columnToUpdate
     * @return
     */
    public Game updateBoard(int gameId, Player player, int columnToUpdate) {
        Optional<Game> optional = games.stream()
                .filter(game -> game.getId() == gameId)
                .findFirst();
        Game game = null;
        if(optional.isPresent()){
            game = optional.get();
            Board updatedBoard = game.getBoard();
            findPossibleRowAndUpdate(updatedBoard, player, columnToUpdate);
        }
        return game;
    }

    /**
     *
     * @param gameId
     * @param player
     * @return
     */
    public Optional<Game> updatePlayerToNextTurn(int gameId, Player player) {
        Optional<Game> optional = games.stream()
                .filter(game -> game.getId() == gameId)
                .findFirst();
        Optional<Game> game = Optional.empty();
        if (optional.isPresent()){
            Game updatedGame = optional.get();
            updatedGame.setPlayerToNextTurn(player);
            game = Optional.of(updatedGame);
        }
        return game;
    }

    /**
     *
     * @param game
     */
    public void removeGame(Game game) {
        this.games.remove(game);
    }

    /**
     *
     * @param board
     * @param player
     * @param columnToUpdate
     */
    private void findPossibleRowAndUpdate(Board board, Player player, int columnToUpdate) {
        BoardDiscColor[][] table = board.getTable();
        int row;
        for (row = board.getTotalRow(); row > 0; row--) {
            if (table[row][columnToUpdate] == BoardDiscColor.NONE ) {
                table[row][columnToUpdate] = player.getColor();
                break;
            }
        }
        if(row == 0) {
            throw new RuntimeException("Invalid Column");
        }
    }
}