package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.helpers.Constants;

public final class Main {
    private static Constants constanta = new Constants();
    private Main() {
        // just to trick checkstyle
    }
    private static void subRound(List<String> PlayerNames, List<Integer> Cards, ArrayList<Player> jucatori, int playerNumber) {
        List<Integer> copy;
        for (int i = 0; i < playerNumber; ++i) {
            copy = Cards.subList(i * constanta.CARDS_PICK, (i + 1) * constanta.CARDS_PICK);
            jucatori.add(new Basic(copy, PlayerNames, i));
            //jucatori.get(i).basicMerchant(jucatori.get(i));

        }
        jucatori.get(0).setJob("sheriff");
        jucatori.get(1).playBasic(jucatori, jucatori.get(1), Cards);
        jucatori.get(0).playBasic(jucatori, jucatori.get(0), Cards);
        jucatori.get(0).setJob("merchant");
        jucatori.get(1).setJob("sheriff");
        jucatori.get(0).playBasic(jucatori, jucatori.get(0), Cards);
        jucatori.get(1).playBasic(jucatori, jucatori.get(1), Cards);

        for (int i = 0; i < playerNumber; ++i) {
            System.out.println(jucatori.get(i));
        }
    }

    public static void main(final String[] args) {
        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/1round2players-mixed-test8.in";
        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/1round2players-mixed-test8.out";

        GameInputLoader gameInputLoader = new GameInputLoader(input, output);
        GameInput gameInput = gameInputLoader.load();

        //TODO implement homework logic
        int rounds = gameInput.getRounds();
        List<String> PlayerNames = gameInput.getPlayerNames();
        List<Integer> Cards = gameInput.getAssetIds();

        int noPlayers = PlayerNames.size();
        ArrayList<Player> jucatori = new ArrayList<Player>();

        subRound(PlayerNames, Cards, jucatori, noPlayers);
        GoodsFactory obiecte = GoodsFactory.getInstance();
        Map<Integer, Goods> harta = obiecte.getAllGoods();
    }
}
