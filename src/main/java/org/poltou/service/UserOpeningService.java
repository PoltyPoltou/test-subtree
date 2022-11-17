package org.poltou.service;

import java.util.List;

import org.poltou.business.UserResult;
import org.poltou.business.opening.user.UserChessNode;
import org.poltou.business.opening.user.UserOpening;
import org.poltou.business.repository.UserNodeRepo;
import org.poltou.business.repository.UserOpeningRepo;
import org.poltou.service.data.UserNodeDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.Board;
import chess.Color;
import chess.Situation;
import chess.format.pgn.InitialPosition;
import chess.format.pgn.ParsedPgn;
import chess.format.pgn.Reader;
import chess.format.pgn.Sans;
import chess.format.pgn.Tags;
import chess.variant.Variant;
import scala.Option;
import scala.Tuple3;
import scala.util.Either;

@Service
public class UserOpeningService {
    Logger logger = LoggerFactory.getLogger(UserOpening.class);
    @Autowired
    UserOpeningRepo userOpeningRepo;
    @Autowired
    UserNodeRepo userNodeRepo;
    @Autowired
    ChesscomImportService chesscomService;
    @Autowired
    UserNodeDataService userNodeDataService;

    public void importChessCom(String username, String color) {
        UserOpening opening = getOrCreateOpening(username, color);
        // opening != null
        List<ParsedPgn> pgnFromPlayer = chesscomService.getPgnFromPlayer(2021, 1, username);
        for (ParsedPgn pgn : pgnFromPlayer) {
            addUserGameToOpening(opening, pgn);
        }

    }

    private UserOpening getOrCreateOpening(String username, String color) {
        UserOpening opening = userOpeningRepo.findByUsernameAndColor(username, color);
        if (opening == null) {
            UserChessNode node = userNodeDataService.createNode(new Situation(Board.init(Variant.orDefault("standard")), Color.White$.MODULE$));
            opening = UserOpening.of(username, color, node);
        }
        return opening;
    }

    private void addUserGameToOpening(UserOpening opening, ParsedPgn pgn) {
        String username = opening.getUsername();
        Tuple3<InitialPosition, Tags, Sans> tuple;
        tuple = ParsedPgn.unapply(pgn).get();
        Tags tags = tuple._2();
        Option<Option<Color>> resultColor = tags.resultColor();
        if (resultColor.nonEmpty()) {
            Color played = null;
            if (tags.apply("White").equals(Option.apply(username))) {
                // user is playing as white
                played = Color.White$.MODULE$;
            } else if (tags.apply("Black").equals(Option.apply(username))) {
                // user is playing as black
                played = Color.Black$.MODULE$;
            } else {
                logger.warn("one pgn loaded does not contain username " + username);
                return;
            }
            UserResult result = UserResult.parseTag(played, resultColor.get());
            UserChessNode nodeIter = opening.getStartingNode();
            Reader.fullWithSans(pgn, id -> id).valid().toOption().get().chronoMoves()
                    .foldLeft(nodeIter, (UserChessNode iter, Either<chess.Move, chess.Drop> dropOrMove) -> {
                        String uci = dropOrMove.fold(mv -> mv.toUci().uci(), drop -> drop.toUci().uci());
                        return userNodeDataService.getOrCreateChildNode(iter, uci, result);
                    });
        }
    }

    private void addChildToParent(UserChessNode parent, UserChessNode childNode) {
        parent.getChildren().put(childNode.getUci(), childNode);
        childNode.setParent(parent);
    }
}
