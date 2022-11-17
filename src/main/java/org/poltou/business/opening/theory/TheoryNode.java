package org.poltou.business.opening.theory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.poltou.business.convertors.BoardConvertor;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import chess.Color;
import chess.Move;
import chess.Pos;
import chess.Situation;
import chess.format.Forsyth;
import scala.Function1;
import scala.collection.JavaConverters;
import scala.jdk.CollectionConverters;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class TheoryNode {
    @Id
    @JsonProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Convert(converter = BoardConvertor.class)
    @Column(name = "fen")
    @JsonIgnore
    private Situation situation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Map<String, TheoryNode> children;

    @ManyToOne
    @JsonIgnore
    private TheoryNode parent;

    private String san;
    private String uci;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUci() {
        return uci;
    }

    public void setUci(String uci) {
        this.uci = uci;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public void setChildren(Map<String, TheoryNode> children) {
        this.children = children;
    }

    public Map<String, TheoryNode> getChildren() {
        return children;
    }

    public TheoryNode getParent() {
        return parent;
    }

    public void setParent(TheoryNode parent) {
        this.parent = parent;
    }

    @JsonGetter(value = "fen")
    public String getFen() {
        return Forsyth.$greater$greater(situation);
    }

    @JsonGetter(value = "parentId")
    public Long getParentId() {
        return this.parent == null ? -1 : this.parent.getId();
    }

    @JsonGetter(value = "color")
    public String getTurn() {
        return situation.color().equals(Color.White$.MODULE$) ? "white" : "black";
    }

    @JsonGetter(value = "children")
    public List<TheoryNode> getChildrenList() {
        return this.children.values().stream().sorted((n1, n2) -> n1.getSan().compareTo(n2.getSan()))
                .collect(Collectors.toList());
    }

    @JsonGetter(value = "moves")
    public Map<String, List<String>> getAvailableMoves() {
        Map<String, List<String>> moves = new HashMap<>();
        scala.collection.immutable.Map<Pos, scala.collection.immutable.List<Move>> movesMap = situation.moves();
        CollectionConverters.MapHasAsJava(movesMap).asJava().forEach(
                (Pos p, scala.collection.immutable.List<Move> mvLst) -> {
                    moves.put(p.key(), JavaConverters.asJava(
                            mvLst.map(
                                    new Function1<Move, String>() {
                                        @Override
                                        public String apply(Move mv) {
                                            return mv.toUci().uci().substring(2, 4);
                                        }

                                    })));
                });
        return moves;
    }
}
