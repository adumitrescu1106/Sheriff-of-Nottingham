package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import com.tema1.goods.GoodsFactory;
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
        for (int i = 0; i < rounds; ++i) {
            for (int j = 0; j < playerNumber; ++j) {
                jucatori.get(j).setCardsInHand(cards, j);
            }
        }
        jucatori.get(0).setJob("sheriff");
        jucatori.get(1).playBasic(jucatori, jucatori.get(1), cards);
        jucatori.get(0).playBasic(jucatori, jucatori.get(0), cards);
        jucatori.get(0).setJob("merchant");
        jucatori.get(1).setJob("sheriff");
        jucatori.get(0).playBasic(jucatori, jucatori.get(0), cards);
        jucatori.get(1).playBasic(jucatori, jucatori.get(1), cards);


    }

    public static void main(final String[] args) {
        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/1round2players-mixed-test8.in";
        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/1round2players-mixed-test8.out";

        GameInputLoader gameInputLoader = new GameInputLoader(input, output);
        GameInput gameInput = gameInputLoader.load();

        //TODO implement homework logic
        int rounds = gameInput.getRounds();
        List<String> playerNames = gameInput.getPlayerNames();
        List<Integer> cards = gameInput.getAssetIds();

        int noPlayers = playerNames.size();
        ArrayList<Player> jucatori = new ArrayList<Player>();

        playGame(playerNames, cards, jucatori, noPlayers, rounds);

        GoodsFactory obiecte = GoodsFactory.getInstance();

        // king bonus assign
        LegalGoods legalGoods;
        int maxim = 1;
        Map<Integer, ArrayList<Integer>> clasament = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < jucatori.size(); ++i) {
            Collections.sort(jucatori.get(i).getBooth());
            for (int j = 0; j <= jucatori.get(i).getBooth().size() - 1; j++) {
                // daca nu exista itemul in map, il creeaza, altfel creste
                if (clasament.get(jucatori.get(i).getBooth().get(j)) == null) {
                    clasament.put(jucatori.get(i).getBooth().get(j), new ArrayList<Integer>());
                    clasament.get(jucatori.get(i).getBooth().get(j)).add(i);
                    clasament.get(jucatori.get(i).getBooth().get(j)).add(1);
                    maxim = 1;
                } else {
                    maxim++;
                }
                if (j != jucatori.get(i).getBooth().size() - 1) {
                    if (!(jucatori.get(i).getBooth().get(j).equals(jucatori.get(i).getBooth().get(j + 1)))) {
                        if (clasament.get(jucatori.get(i).getBooth().get(j)).get(1) < maxim) {
                            clasament.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                        }
                    }
                } else {
                    if (jucatori.get(i).getBooth().size() == 1) {
                        maxim = 1;
                        clasament.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                    } else {
                        clasament.get(jucatori.get(i).getBooth().get(j)).set(1, maxim);
                    }
                }
            }
        }
        for (Map.Entry<Integer, ArrayList<Integer>> entry : clasament.entrySet()) {
            legalGoods = (LegalGoods) obiecte.getGoodsById(entry.getKey());
            jucatori.get(entry.getValue().get(0)).addCoins(legalGoods.getKingBonus());
        }

        for (int i = 0; i < noPlayers; ++i) {
            System.out.println(jucatori.get(i));
        }
    }
}
