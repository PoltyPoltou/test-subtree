package org.poltou.business.opening.theory;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class TheoryOpening {

    public static TheoryOpening of(String name, TheoryNode startingNode) {
        TheoryOpening theoryOpening = new TheoryOpening();
        theoryOpening.setName(name);
        theoryOpening.setStartingNode(startingNode);
        return theoryOpening;
    }

    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private TheoryNode startingNode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TheoryNode getStartingNode() {
        return startingNode;
    }

    public void setStartingNode(TheoryNode startingNode) {
        this.startingNode = startingNode;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty
    public String getColor() {
        return startingNode.getTurn();
    }

}
