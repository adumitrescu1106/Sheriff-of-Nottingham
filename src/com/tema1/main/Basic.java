package com.tema1.main;

import java.util.ArrayList;

public abstract class Basic extends Player {


    void playBasic(Player player) {
        if (player.getJob() == "merchant") {
            basicMerchant(player);
        } else {
            basicSheriff(player);
        }
    }

    public void basicMerchant(Player player) {
        ArrayList<Integer> frequency = new ArrayList<Integer>();

    }

    public void basicSheriff(Player player) {

    }


}
