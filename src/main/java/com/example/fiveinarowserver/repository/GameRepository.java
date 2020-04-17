package com.example.fiveinarowserver.repository;

import com.example.fiveinarowserver.model.board.BoardDiscColor;
import com.example.fiveinarowserver.model.game.GameStatus;
import com.example.fiveinarowserver.model.board.Board;
import com.example.fiveinarowserver.repository.entity.Game;
import com.example.fiveinarowserver.repository.entity.Player;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 */
@Log4j2
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

    /**
     * Check if the board in win state.
     *
     * @param game
     * @return true if winning state.
     */
    public boolean didWin(final Game game) {
        Board board = game.getBoard();
        BoardDiscColor[][] table = board.getTable();
        BoardDiscColor colorToFind = game.getPlayerToNextTurn().getColor();
        for (int i = 1; i <= board.getTotalRow(); i++) {
            for (int j = 1; j <= board.getTotalColumn(); j++) {
                if (table[i][j] == colorToFind) {
                    if (hasVerticalWinState(board, i, j, colorToFind)) {
                        return true;
                    } else if (hasHorizontalWinState(board, i, j, colorToFind)) {
                        return true;
                    } else if (hasFirstDiagonalWinState(board, i, j, colorToFind)) {
                        log.info(i + "," + j);
                        return true;
                    } else if (hasSecondDiagonalWinState(board, i, j, colorToFind)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param game
     * @return
     */
    public boolean isMatchTied(final Game game) {
        BoardDiscColor[][] table = game.getBoard().getTable();
        for (int i = 1; i <= game.getBoard().getTotalRow(); i++) {
            for (int j = 1; j <= game.getBoard().getTotalColumn(); j++) {
                if(table[i][j] == BoardDiscColor.NONE) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean hasVerticalWinState(
            final Board board, final int x, final int y, final BoardDiscColor colorToFind) {
        int count = 1;
        for (int i = x; i < board.getTotalRow(); i++) {
            if (board.getTable()[i + 1][y] == colorToFind) {
                count = count + 1;
            } else {
                break;
            }
        }
        for (int i = x ; i > 1; i--) {
            if (board.getTable()[i - 1][y] == colorToFind) {
                count = count + 1;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }
        return false;
    }

    private boolean hasHorizontalWinState(
            final Board board, final int x, final int y, final BoardDiscColor colorToFind) {
        int count = 1;
        for (int i = y; i < board.getTotalColumn(); i++) {
            if (board.getTable()[x][i + 1] == colorToFind) {
                count = count + 1;
            } else {
                break;
            }
        }
        for (int i = y ; i > 1; i--) {
            if (board.getTable()[x][i - 1] == colorToFind) {
                count = count + 1;
            } else {
                break;
            }
        }
        if (count == 5) {
            return true;
        }
        return false;
    }

    private boolean hasFirstDiagonalWinState(
            final Board board, final int x, final int y, final BoardDiscColor colorToFind) {
        int count = 1;
        int i = x;
        int j = y;
        while(i < board.getTotalRow() && j < board.getTotalColumn()) {
            if (board.getTable()[++i][++j] == colorToFind) {
                count = count + 1;
            } else{
                break;
            }
        }
        i = x;
        j = y;
        while(i > 1 && j > 1) {
            if (board.getTable()[--i][--j] == colorToFind) {
                count = count + 1;
            } else{
                break;
            }
        }

        if (count == 5) {
            return true;
        }
        return false;
    }

    private boolean hasSecondDiagonalWinState(
            final Board board, final int x, final int y, final BoardDiscColor colorToFind) {
        int count = 1;
        int i = x;
        int j = y;
        while(i < board.getTotalRow() && j > 1) {
            if (board.getTable()[++i][--j] == colorToFind) {
                count = count + 1;
            } else{
                break;
            }
        }
        i = x;
        j = y;
        while(i > 1 && j < board.getTotalColumn()) {
            if (board.getTable()[--i][++j] == colorToFind) {
                count = count + 1;
            } else{
                break;
            }
        }

        if (count == 5) {
            return true;
        }
        return false;
    }
}