package com.example.fiveinarowserver.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import static java.util.Arrays.fill;
import static com.example.fiveinarowserver.model.board.BoardDiscColor.NONE;

@Data
@Builder
@AllArgsConstructor
@Log4j2
public class Board {
    @Value("${game.board.totalColum:9}")
    private int totalColumn;

    @Value("${game.board.totalRow:6}")
    private int totalRow;

    private final BoardDiscColor[][] table;

    public Board(final int totalRow, int totalColumn) {
        this.totalRow = totalRow;
        this.totalColumn = totalColumn;
        table = new BoardDiscColor[this.totalRow + 1][this.totalColumn + 1];
        for (BoardDiscColor[] row: table) {
                fill(row, NONE);
        }
    }
}
