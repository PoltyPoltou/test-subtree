package org.poltou.controller;

import java.util.List;

import org.poltou.business.opening.theory.TheoryNode;
import org.poltou.business.opening.theory.TheoryOpening;
import org.poltou.controller.datainterface.ChessNodeDataInterface;
import org.poltou.controller.datainterface.OpeningDataInterface;
import org.poltou.exceptions.BadIdException;
import org.poltou.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpeningController {
    @Autowired
    private OpeningService openingService;

    @GetMapping("/opening")
    public List<TheoryOpening> getAllOpening() {
        return openingService.getAllOpenings();
    }

    @GetMapping("/opening/{id}")
    public TheoryOpening getOpeningById(@PathVariable Long id) {
        return openingService.findOpeningById(id).orElseThrow(() -> new BadIdException(id.toString()));
    }

    @GetMapping("/chessnode/{id}")
    public TheoryNode getNodeById(@PathVariable Long id) {
        return openingService.findNodeById(id).orElseThrow(() -> new BadIdException(id.toString()));
    }

    @PostMapping("/opening")
    public Long addOpening(@RequestBody OpeningDataInterface opening) {
        return openingService.addOpening(opening.getName(), opening.getFen());
    }

    @PostMapping("/chessnode/{id}")
    public Long addNode(@PathVariable Long id, @RequestBody ChessNodeDataInterface node) {
        return openingService.addNodeToOpening(id, node.getUci());
    }

    @DeleteMapping("/opening/{id}")
    public void deleteOpening(@PathVariable Long id) {
        openingService.deleteOpening(id);
    }

    @DeleteMapping("/chessnode/{id}")
    public void deleteChessNode(@PathVariable Long id) {
        openingService.deleteChessNode(id);
    }
}
