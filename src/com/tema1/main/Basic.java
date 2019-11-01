package com.tema1.main;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.helpers.Constants;

import java.util.ArrayList;
import java.util.List;


public abstract class Basic extends Player {
    private Constants constants;
    private GoodsFactory products;
    public Basic() {
        super();
        this.products = GoodsFactory.getInstance();
    }

    public Basic(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        super(cardsInHand, PlayerNames, i);
        this.products = GoodsFactory.getInstance();
    }

    public final void playBasic(Player player) {
        if (player.getJob().equals("merchant")) {
            basicMerchant(player);
        } else {
            basicSheriff(player);
        }
    }

    public final void basicMerchant(Player player) {
        ArrayList<Integer> frequency = new ArrayList<Integer>();
        if (!(checkIfLegal(getCardsInHand()))) {
            getCardMaxProfit(getCardsInHand());
        }
    }

    public void basicSheriff(Player player) {

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

    }

}
