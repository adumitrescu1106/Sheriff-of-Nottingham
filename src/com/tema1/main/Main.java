package com.tema1.main;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.LegalGoods;
import com.tema1.helpers.Constants;


public final class Main {
    private static Constants constanta = new Constants();

    private Main() {
        // just to trick checkstyle
    }
    private static void playGame(List<String> playerNames, List<Integer> cards, ArrayList<Player> jucatori, int playerNumber, int rounds) {
        //initializez arraylistul de jucatori
        for (int i = 0; i < playerNumber; ++i) {
            jucatori.add(new Basic(playerNames, i));
        }
        //desfasurarea jocului
        int indexCarti = 0;
        int sheriffIndex;
        for (int i = 1; i <= rounds; ++i) {
            //subrundele
            sheriffIndex = 0;
            for (int k = 0; k < playerNumber; k++) {
                //impartirea cartilor
                for (int j = 0; j < playerNumber; j++) {
                    //setez sherifful subrundei
                    if (j == sheriffIndex) {
                        jucatori.get(j).setJob("sheriff");
                    } else {
                        //jucatorul j primeste cartile si isi face sacul
                        jucatori.get(j).setCardsInHand(cards, indexCarti);
                        indexCarti++;
                        jucatori.get(j).playBasic(jucatori, jucatori.get(j), cards, i);
                        //System.out.println(jucatori.get(j));
                    }
                    //System.out.println(jucatori.get(j));
                }
                // politistul subrundei isi face verificarea
                jucatori.get(sheriffIndex).playBasic(jucatori, jucatori.get(sheriffIndex), cards, i);
                //System.out.println(jucatori.get(k));
//                for (int r = 0; r < playerNumber; r++) {
//                    System.out.println(jucatori.get(r));
//                }
                jucatori.get(sheriffIndex).setJob("merchant");
                sheriffIndex++;
                clearBags(jucatori);
            }
            //clearBags(jucatori);
        }
    }

    private static void clearBags(ArrayList<Player> jucatori) {
        for (Player player : jucatori) {
            player.getBag().clear();
        }
    }

    public static void main(final String[] args) {
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/2round3players-mixed-test7.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/2round3players-mixed-test7.out";
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/2round2players-legal-only-test9.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/2round2players-legal-only-test9.out";
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/1round3players-legal-only-test7.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/1round3players-legal-only-test7.out";

////
//        GameInputLoader gameInputLoader = new GameInputLoader(input, output);
//        GameInput gameInput = gameInputLoader.load();

        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();


        //TODO implement homework logic
        int rounds = gameInput.getRounds();
        List<String> playerNames = gameInput.getPlayerNames();
        List<Integer> cards = gameInput.getAssetIds();

        int noPlayers = playerNames.size();
        ArrayList<Player> jucatori = new ArrayList<Player>();

        playGame(playerNames, cards, jucatori, noPlayers, rounds);

        GoodsFactory obiecte = GoodsFactory.getInstance();

        for (int i = 0; i < noPlayers; ++i) {
            for (int k = 0; k < jucatori.get(i).getBooth().size(); k++) {
                jucatori.get(i).addCoins(obiecte.getGoodsById(jucatori.get(i).getBooth().get(k)).getProfit());
            }
        }

        // KING BONUS ASSIGN
        LegalGoods legalGoods;
        int maxKing = 0;
        int indexMaxKing = 0;
        int maxQueen = 0;
        int indexMaxQueen = 0;
        int maxim = 0;
        for (int k = 0; k <= 9; k++) {
            //iterez prin playeri
            maxKing = 0;
            indexMaxKing = 0;
            maxQueen = 0;
            indexMaxQueen = 0;
            for (int i = 0; i < jucatori.size(); ++i) {
                //iterez prin tarabele lor
                maxim = 0;
                for (int j = 0; j <= jucatori.get(i).getBooth().size() - 1; j++) {
                    if (k == jucatori.get(i).getBooth().get(j)) {
                        maxim++;
                    }
                }
                if (maxim > maxKing) {
                    maxKing = maxim;
                    indexMaxKing = i;
                }
//             maxim = 0;

            }
            for (int i = 0; i < jucatori.size(); ++i) {
                maxim = 0;
                for (int r = 0; r <= jucatori.get(i).getBooth().size() - 1; r++) {
                    if (i != indexMaxKing && k == jucatori.get(i).getBooth().get(r)) {
                        maxim++;
                    }
                }
                if (maxim > maxQueen) {
                    maxQueen = maxim;
                    indexMaxQueen = i;
                }
            }
            if (maxKing != 0) {
                legalGoods = (LegalGoods) obiecte.getGoodsById(k);
                jucatori.get(indexMaxKing).addCoins(legalGoods.getKingBonus());
            }
            if (maxQueen != 0) {
                legalGoods = (LegalGoods) obiecte.getGoodsById(k);
                jucatori.get(indexMaxQueen).addCoins(legalGoods.getQueenBonus());
            }
            //System.out.println(maxQueen + " "+ indexMaxQueen);
        }


       // printeaza jucatorii
//        for (int i = noPlayers - 1; 0 <= i; i--) {
//            System.out.println(jucatori.get(i));
//
//        }
//        int k = 0;
//        for (int i = noPlayers - 1; 0 <= i; i--) {
//            if (jucatori.get(i).getTactic().equals("basic")) {
//                System.out.println(k + " BASIC " + jucatori.get(i).getCoins());
//                k++;
//            }
//            if (jucatori.get(i).getTactic().equals("greedy")) {
//                System.out.println(k + " GREEDY " + jucatori.get(i).getCoins());
//                k++;
//            }
//       }
        jucatori.sort(Player::compareTo);
        for (int i = 0; i < noPlayers; i++) {
            if (jucatori.get(i).getTactic().equals("basic")) {
                System.out.println(jucatori.get(i).getPlayerindex() + " BASIC " + jucatori.get(i).getCoins());
            }
            if (jucatori.get(i).getTactic().equals("greedy")) {
                System.out.println(jucatori.get(i).getPlayerindex() + " GREEDY " + jucatori.get(i).getCoins());
            }
            if (jucatori.get(i).getTactic().equals("bribed")) {
                System.out.println(jucatori.get(i).getPlayerindex() + " BRIBED " + jucatori.get(i).getCoins());
            }
        }
    }
}
