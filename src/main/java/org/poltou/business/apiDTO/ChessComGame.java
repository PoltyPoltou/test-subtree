package org.poltou.business.apiDTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ChessComGameSerializer.class)
public class ChessComGame {
    private final String pgn;

    public String getPgn() {
        return pgn;
    }

    public ChessComGame(String pgn) {
        this.pgn = pgn;
    }
}
