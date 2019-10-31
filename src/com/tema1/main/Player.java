package com.tema1.main;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    public ArrayList<Integer> bag;
    public int coins = 80;
    public List<Integer> CardsInHand;
    public String declaration;
    public String tactic;

    public Player(){}

    public Player(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        CardsInHand = cardsInHand;
        this.tactic = PlayerNames.get(i);

    }

    abstract void play_basic(Player player);
    abstract void play_bribed(Player player);
    abstract void play_greedy(Player player);



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
