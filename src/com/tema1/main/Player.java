package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import com.tema1.helpers.Constants;

public class Player {
    private ArrayList<Integer> bag;
    private ArrayList<Integer> booth;
    private Constants constanta = new Constants();
    private int coins;
    private List<Integer> cardsInHand;
    private int declaration;
    private String tactic;
    private String job;

    public Player() {
        coins = constanta.START_BUDGET;
        bag = new ArrayList<>();
        booth = new ArrayList<>();
    }

    Player(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        coins = constanta.START_BUDGET;
        this.cardsInHand = cardsInHand;
        this.tactic = PlayerNames.get(i);
        this.job = "merchant";
        bag = new ArrayList<>();
        booth = new ArrayList<>();

    }

    public final ArrayList<Integer> getBag() {
        return bag;
    }

    public final ArrayList<Integer> getBooth() {
        return booth;
    }

    public final int getCoins() {
        return coins;
    }

    public final List<Integer> getCardsInHand() {
        return cardsInHand;
    }

    public final int getDeclaration() {
        return declaration;
    }

    public final String getTactic() {
        return tactic;
    }

    public final String getJob() {
        return job;
    }

    public final void setJob(String job) {
        this.job = job;
    }

    public final void setDeclaration(int id) {
        this.declaration = id;
    }
    public final void addCoins(int coinsToAdd) {
        this.coins = this.coins + coinsToAdd;
    }

    public final void subCoins(int coinsToSub) {
        this.coins = this.coins - coinsToSub;
    }

    public void basicMerchant(Player player) {

    }

    public void playBasic(ArrayList<Player> jucatori, Player player, List<Integer> cards) {

    }



    public final String toString() {
        return "Player{"
               + "bag=" + bag
               + ", coins=" + coins + ", CardsInHand=" + cardsInHand
                + ", declaration='" + declaration + '\''
                + ", tactic='" + tactic + '\''
                + '}';
    }
}
