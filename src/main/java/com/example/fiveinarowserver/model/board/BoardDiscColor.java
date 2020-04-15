package com.example.fiveinarowserver.model.board;

public enum BoardDiscColor {
    NONE("N"),

    PLAYER_X("X"),

    PLAYER_O("O");

    private String type;

    private BoardDiscColor(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
