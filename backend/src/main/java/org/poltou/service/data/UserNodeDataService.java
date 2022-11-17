package org.poltou.service.data;

import org.poltou.business.UserResult;
import org.poltou.business.opening.theory.TheoryNode;
import org.poltou.business.opening.user.UserChessNode;
import org.springframework.stereotype.Service;

import chess.Situation;

@Service
public class UserNodeDataService extends TheoryNodeDataService {

    @Override
    protected TheoryNode provideNode() {
        return new UserChessNode();
    }

    private void setupNode(UserChessNode node) {
        node.setEncounters(0);
        node.setWins(0);
        node.setLosses(0);
    }

    public UserChessNode createNode(Situation situation) {
        UserChessNode node = (UserChessNode) super.createNode(situation);
        setupNode(node);
        return node;
    }

    public UserChessNode createNode(Situation situation, UserChessNode parent, String san, String uci) {
        UserChessNode node = (UserChessNode) super.createNode(situation, parent, san, uci);
        setupNode(node);
        return node;
    }

    public UserChessNode getOrCreateChildNode(UserChessNode parent, String uci, UserResult result) {
        UserChessNode foundNode = (UserChessNode) super.getOrCreateChildNode(parent, uci);
        foundNode.setEncounters(foundNode.getEncounters() + 1);
        switch (result) {
            case WIN:
                foundNode.setWins(foundNode.getWins() + 1);
                break;
            case LOSS:
                foundNode.setLosses(foundNode.getLosses() + 1);
                break;
            default:
        }
        return foundNode;
    }
}
