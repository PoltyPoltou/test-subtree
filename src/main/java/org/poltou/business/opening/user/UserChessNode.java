package org.poltou.business.opening.user;

import javax.persistence.Entity;

import org.poltou.business.opening.theory.TheoryNode;

import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
public class UserChessNode extends TheoryNode {

    private int encounters;
    private int wins;
    private int losses;

    public int getEncounters() {
        return encounters;
    }

    public void setEncounters(int encounters) {
        this.encounters = encounters;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    @JsonGetter
    public int getDraws() {
        return this.encounters - this.wins - this.losses;
    }
}
