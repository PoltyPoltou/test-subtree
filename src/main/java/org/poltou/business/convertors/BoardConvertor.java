package org.poltou.business.convertors;

import javax.persistence.AttributeConverter;

import chess.Situation;
import chess.format.Forsyth;

/**
 * BoardConvertor
 */
public class BoardConvertor implements AttributeConverter<Situation, String> {

    @Override
    public String convertToDatabaseColumn(Situation javaKey) {
        return Forsyth.$greater$greater(javaKey);
    }

    @Override
    public Situation convertToEntityAttribute(final String databaseKey) {
        return Forsyth.$less$less(databaseKey).get();
    }

}