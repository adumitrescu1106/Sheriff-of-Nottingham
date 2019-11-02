package com.tema1.main;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.helpers.Constants;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Basic extends Player {
    private Constants constants;
    private GoodsFactory products;
    private Map<Integer, Integer> frequency;
    public Basic() {
        super();
        this.products = GoodsFactory.getInstance();
        this.frequency = new HashMap<Integer, Integer>();
    }

    public Basic(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        super(cardsInHand, PlayerNames, i);
        this.products = GoodsFactory.getInstance();
        this.frequency = new HashMap<Integer, Integer>();
    }

    public final void playBasic(Player player) {
        if (player.getJob().equals("merchant")) {
            basicMerchant(player);
        } else {
            basicSheriff(player);
        }
    }

    public final void basicMerchant(Player player) {
        if (!(checkIfLegal(player.getCardsInHand()))) {
            getCardMaxProfit(player.getCardsInHand());
        } else {
          getBestItem(player.getCardsInHand());
        }
    }

    public void basicSheriff(Player player) {

    }

    public final void getBestItem(List<Integer> cardsInHand) {
        this.frequency.put(cardsInHand.get(0), 1);
        for (int i = 0; i < cardsInHand.size() - 1; ++i) {
            if (cardsInHand.get(i).equals(cardsInHand.get(i+1))) {
                this.frequency.put(cardsInHand.get(i), this.frequency.get(cardsInHand.get(i)) + 1);
            } else {
                this.frequency.put(cardsInHand.get(i+1), this.frequency.get(cardsInHand.get(i+1)) + 1);
            }

        }
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        for (int i = 0; i < maxEntry.getValue(); ++i) {
            this.getBag().add(maxEntry.getKey());
        }
    }

    public final boolean checkIfLegal(List<Integer> cardsInHand) {
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (cardsInHand.get(i) <= constants.LEGAL_BOUND) {
                return true;
            }
        }
        return false;
    }

    public final void getCardMaxProfit(List<Integer> cardsInHand) {
        int max = cardsInHand.get(0);
        Goods aux = products.getGoodsById(max);
        for (int i = 1; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getProfit() > aux.getProfit()) {
                max = cardsInHand.get(i);
            }
        }
        this.getBag().add(max);
        setDeclaration("mere");
        // adauga cartea ilegala cu profitul cel mai mare in sac si o declara ca fiind mere
    }

}
