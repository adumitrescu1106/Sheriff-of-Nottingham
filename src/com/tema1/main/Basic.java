package com.tema1.main;

import java.util.ArrayList;
import java.util.List;


public abstract class Basic extends Player {

    public Basic() {
        super();
    }

    public Basic(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        super(cardsInHand, PlayerNames, i);
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

    }

    public void basicSheriff(Player player) {

    }


}
