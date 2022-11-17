package org.poltou.service.data;

import java.util.HashMap;

import org.poltou.business.opening.theory.TheoryNode;
import org.springframework.stereotype.Service;

import cats.data.Validated;
import chess.Move;
import chess.Situation;
import chess.format.Uci;
import chess.format.pgn.Dumper;

@Service
public class TheoryNodeDataService {

    protected TheoryNode provideNode() {
        return new TheoryNode();
    }

    public TheoryNode createNode(Situation situation) {
        return createNode(situation, null, "ROOT", "");
    }

    public TheoryNode createNode(Situation situation, TheoryNode parent, String san, String uci) {
        TheoryNode node = provideNode();
        node.setSituation(situation);
        node.setChildren(new HashMap<>());
        node.setSan(san);
        node.setUci(uci);
        return node;
    }

    public TheoryNode getOrCreateChildNode(TheoryNode parent, String uci) {
        TheoryNode foundNode = parent.getChildren().get(uci);
        if (foundNode == null) {
            foundNode = createChildNode(parent, uci);
        }
        return foundNode;
    }

    /**
     * @param parent
     * @param uci    uci move leading parent to child to create
     * @return child node newly created
     */
    public TheoryNode createChildNode(TheoryNode parent, String uci) {
        Validated<String, Move> elmt = parent.getSituation().move(Uci.Move$.MODULE$.apply(uci).get());
        // validated element, left is error (String), right is ok (Move)
        return elmt.fold((str) -> {
            throw new IllegalArgumentException(str);
        }, (move) -> {
            Situation situation = move.situationAfter();
            String san = Dumper.apply(move);
            String uciParsed = move.toUci().uci();
            TheoryNode childNode = createNode(situation, parent, san, uciParsed);
            addChildToParent(parent, childNode);
            return childNode;
        });
    }

    private void addChildToParent(TheoryNode parent, TheoryNode childNode) {
        parent.getChildren().put(childNode.getUci(), childNode);
        childNode.setParent(parent);
    }
}
