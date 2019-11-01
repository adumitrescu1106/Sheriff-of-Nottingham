package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import com.tema1.helpers.Constants;

public  class Player {
    private ArrayList<Integer> bag;
    private ArrayList<Integer>
    private Constants constanta = new Constants();
    private int coins = constanta.START_BUDGET;
    private List<Integer> CardsInHand;
    private String declaration;
    private String tactic;
    private String job;

    public Player() {

    }

    Player(List<Integer> cardsInHand, List<String> PlayerNames, int i) {

        CardsInHand = cardsInHand;
        this.tactic = PlayerNames.get(i);

    }

    public final String getJob() {
        return job;
    }

    public final String getDeclaration() {
        return declaration;
    }

    @Override
    public String toString() {
        return "Player{" +
                "bag=" + bag +
                ", coins=" + coins +
                ", CardsInHand=" + CardsInHand +
                ", declaration='" + declaration + '\'' +
                ", tactic='" + tactic + '\'' +
                '}';
    }
}
