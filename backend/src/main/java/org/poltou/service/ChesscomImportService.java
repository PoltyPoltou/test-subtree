package org.poltou.service;

import java.util.LinkedList;
import java.util.List;

import org.poltou.business.apiDTO.ChessComGame;
import org.poltou.business.apiDTO.ChessComMonthlyGames;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import chess.format.pgn.ParsedPgn;
import chess.format.pgn.Parser;

@Service
public class ChesscomImportService {
    public final static String CHESSCOM_API = "https://api.chess.com/pub";

    private WebClient client;

    public ChesscomImportService() {
        this.client = WebClient.builder()
                .baseUrl(CHESSCOM_API)
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                                .build())
                .build();
    }

    public List<ParsedPgn> getPgnFromPlayer(int year, int month, String username) {
        String monthStr = month > 9 ? Integer.toString(month) : "0" + month;
        ChessComMonthlyGames monthlyGames = client.get().uri("/player/" + username + "/games/" + year + "/" + monthStr)
                .retrieve()
                .bodyToMono(ChessComMonthlyGames.class).block();
        List<ParsedPgn> games = new LinkedList<>();
        for (ChessComGame game : monthlyGames.getGames()) {
            ParsedPgn parsedPgn = Parser.full(game.getPgn()).toOption().get();
            games.add(parsedPgn);
        }
        return games;
    }

}
