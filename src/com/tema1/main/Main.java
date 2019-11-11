package com.tema1.main;

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
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/2round2players-mixed-test2.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/2round2players-mixed-test2.out";
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/2round2players-legal-only-test9.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/2round2players-legal-only-test9.out";
//        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/1round2players-legal-only-test2.in";
//        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/1round2players-legal-only-test2.out";

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

//        for (int i = 0; i < noPlayers; ++i) {
//            for (int k = 0; k < jucatori.get(i).getBooth().size(); k++) {
//                jucatori.get(i).addCoins(obiecte.getGoodsById(jucatori.get(i).getBooth().get(k)).getProfit());
//            }
//        }
        // KING BONUS ASSIGN
        LegalGoods legalGoods;
        int maxim = 1;
        Map<Integer, ArrayList<Integer>> king = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < jucatori.size(); ++i) {
            //sortez tarabele
            maxim = 0;
            Collections.sort(jucatori.get(i).getBooth());
            for (int j = 0; j <= jucatori.get(i).getBooth().size() - 1; j++) {
                // daca nu exista itemul in map, il creeaza, altfel creste
                if (king.get(jucatori.get(i).getBooth().get(j)) == null) {
                    king.put(jucatori.get(i).getBooth().get(j), new ArrayList<Integer>());
                    king.get(jucatori.get(i).getBooth().get(j)).add(jucatori.get(i).getPlayerindex());
                    //System.out.println("max " + maxim);
                    king.get(jucatori.get(i).getBooth().get(j)).add(1);
                    maxim++;
                } else {
                    maxim++;
                }
                if (j != jucatori.get(i).getBooth().size() - 1) {
                    if (!(jucatori.get(i).getBooth().get(j).equals(jucatori.get(i).getBooth().get(j + 1))) ) {
                        if (king.get(jucatori.get(i).getBooth().get(j)).get(1) < maxim) {
                            king.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                            king.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                           // System.out.println("max1 " + maxim);
                            maxim = 1;
                        } else if (king.get(jucatori.get(i).getBooth().get(j)).get(1) == maxim &&
                                jucatori.get(i).getPlayerindex() < king.get(jucatori.get(i).getBooth().get(j)).get(0)) {
                            king.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                            //System.out.println("primul if");
                        }
                    }
                } else {
                    if (jucatori.get(i).getBooth().size() == 1) {
                        maxim = 1;
                        king.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                        king.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                       // System.out.println("max2 " + maxim);
                    } else if (jucatori.get(i).getPlayerindex() < king.get(jucatori.get(i).getBooth().get(j)).get(0)
                    && king.get(jucatori.get(i).getBooth().get(j)).get(1) == maxim) {
                        king.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                       // System.out.println("al doilea if");
                    } else {
                        //maxim++;
                        king.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                        //king.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                       // System.out.println("max3 " + maxim);
                    }
                }
            }
        }
        for (Map.Entry<Integer, ArrayList<Integer>> entry : king.entrySet()) {
            legalGoods = (LegalGoods) obiecte.getGoodsById(entry.getKey());
            //System.out.println("king__key:" + entry.getKey() + " value:" + entry.getValue());
            jucatori.get(entry.getValue().get(0)).addCoins(legalGoods.getKingBonus());
        }

       // QUEEN BONUS ASSIGN
        Map<Integer, ArrayList<Integer>> queen = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < jucatori.size(); ++i) {
            maxim = 0;
            for (int j = 0; j <= jucatori.get(i).getBooth().size() - 1; j++) {
                // daca nu exista itemul in map, il creeaza, altfel creste
                if (jucatori.get(i).getPlayerindex() != king.get(jucatori.get(i).getBooth().get(j)).get(0)) {
                    if (queen.get(jucatori.get(i).getBooth().get(j)) == null
                            ) {
                        queen.put(jucatori.get(i).getBooth().get(j), new ArrayList<Integer>());
                        queen.get(jucatori.get(i).getBooth().get(j)).add(jucatori.get(i).getPlayerindex());
                        queen.get(jucatori.get(i).getBooth().get(j)).add(1);
                       // maxim = 1;
                    } else {
                        maxim++;
                    }
                    if (j != jucatori.get(i).getBooth().size() - 1) {
                        if (!(jucatori.get(i).getBooth().get(j).equals(jucatori.get(i).getBooth().get(j + 1)))) {
                            if (queen.get(jucatori.get(i).getBooth().get(j)).get(1) < maxim && king.get(jucatori.get(i).getBooth().get(j)).get(1) > maxim
                            && jucatori.get(i).getPlayerindex() < queen.get(jucatori.get(i).getBooth().get(j)).get(0)) {
                                queen.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                                queen.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                            }
                        }
                    } else {
                        if (jucatori.get(i).getBooth().size() == 1 ) {
                            maxim = 1;
                            queen.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                            queen.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                        } else {
                            if (king.get(jucatori.get(i).getBooth().get(j)).get(1) > maxim &&
                                    jucatori.get(i).getPlayerindex() < queen.get(jucatori.get(i).getBooth().get(j)).get(0)) {
                                queen.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                                queen.get(jucatori.get(i).getBooth().get(j)).set(0, jucatori.get(i).getPlayerindex());
                            }
                        }
                    }
                }
            }
        }
        for (Map.Entry<Integer, ArrayList<Integer>> entry : queen.entrySet()) {
            legalGoods = (LegalGoods) obiecte.getGoodsById(entry.getKey());
            jucatori.get(entry.getValue().get(0)).addCoins(legalGoods.getQueenBonus());
            //System.out.println("queen_key:" + entry.getKey() + " value:" + entry.getValue());
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
