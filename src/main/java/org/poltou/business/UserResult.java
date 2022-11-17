package org.poltou.business;

import chess.Color;
import scala.Option;

public enum UserResult {
    DRAW, WIN, LOSS;

    /**
     * @param played color played by user
     * @param tag    issued by the parsedpgn
     * @return corresponding result of the color given
     */
    public static UserResult parseTag(Color played, Option<Color> tag) {
        if (tag.equals(Option.empty())) {
            return DRAW;
        } else {
            return tag.equals(Option.apply(played)) ? WIN : LOSS;
        }
    }
}
