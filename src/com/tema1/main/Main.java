package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.helpers.Constants;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        String input = "/home/andrei/Desktop/TemePOO/src/checker/tests/in/1round2players-legal-only-test4.in";
        String output = "/home/andrei/Desktop/TemePOO/src/checker/tests/out/1round2players-legal-only-test4.out";

        GameInputLoader gameInputLoader = new GameInputLoader(input, output);
        GameInput gameInput = gameInputLoader.load();

        //TODO implement homework logic
        int rounds = gameInput.getRounds();
        List<String> PlayerNames = gameInput.getPlayerNames();
        List<Integer> Cards = gameInput.getAssetIds();
        List<Integer> copy;
        Constants constanta = new Constants();

        int noPlayers = PlayerNames.size();
        ArrayList<Player> jucatori = new ArrayList<Player>();

        for (int i = 0; i < PlayerNames.size(); ++i) {
            copy = Cards.subList(i * constanta.CARDS_PICK, (i + 1) * constanta.CARDS_PICK);
            jucatori.add(new Basic(copy, PlayerNames, i));
            jucatori.get(i).basicMerchant(jucatori.get(i));
            System.out.println(jucatori.get(i));
        }
        GoodsFactory obiecte = GoodsFactory.getInstance();
        Map<Integer, Goods> harta = obiecte.getAllGoods();
        Goods ciorba = obiecte.getGoodsById(2);
        System.out.println(ciorba.getPenalty());

    }
}
