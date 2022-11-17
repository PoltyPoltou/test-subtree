package org.poltou.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.poltou.business.opening.theory.TheoryNode;
import org.poltou.business.opening.theory.TheoryOpening;
import org.poltou.business.repository.TheoryNodeRepo;
import org.poltou.business.repository.TheoryOpeningRepo;
import org.poltou.exceptions.BadIdException;
import org.poltou.service.data.TheoryNodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.Situation;
import chess.format.Forsyth;

@Service
public class OpeningService {
    @Autowired
    private TheoryOpeningRepo openingRepo;
    @Autowired
    private TheoryNodeRepo chessNodeRepo;
    @Autowired
    private TheoryNodeDataService theoryNodeDataService;

    public List<TheoryOpening> getAllOpenings() {
        return StreamSupport.stream(openingRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<TheoryOpening> findOpeningById(Long id) {
        return openingRepo.findById(id);
    }

    public Optional<TheoryNode> findNodeById(Long id) {
        return chessNodeRepo.findById(id);
    }

    public Long addOpening(String name, String startingFen) {
        Situation situation = Forsyth.$less$less(startingFen).getOrElse(null);
        if (situation != null) {
            TheoryNode node = theoryNodeDataService.createNode(situation);
            TheoryOpening opening = TheoryOpening.of(name, node);
            return openingRepo.save(opening).getId();
        } else {
            throw new IllegalArgumentException("Fen provided is not parsable");
        }
    }

    public Long addNodeToOpening(Long nodeId, String uci) {
        TheoryNode parent = chessNodeRepo.findById(nodeId)
                .orElseThrow(() -> new BadIdException("ChessNode " + nodeId + " not found."));
        TheoryNode childNode = theoryNodeDataService.getOrCreateChildNode(parent, uci);
        return chessNodeRepo.save(childNode).getId();
    }

    public void deleteOpening(Long id) {
        if (openingRepo.findById(id).isPresent()) {
            openingRepo.deleteById(id);
        } else {
            throw new BadIdException();
        }
    }

    public void deleteChessNode(Long id) {
        Optional<TheoryNode> optNode = chessNodeRepo.findById(id);
        if (optNode.isPresent()) {
            if (optNode.get().getParent() != null) {

                optNode.get().getParent().getChildren().remove(optNode.get().getUci());
                chessNodeRepo.deleteById(id);
            } else {
                throw new IllegalArgumentException("Can't delete root node of opening.");
            }
        } else {
            throw new BadIdException();
        }
    }

}