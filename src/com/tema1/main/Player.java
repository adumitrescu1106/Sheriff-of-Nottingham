package com.tema1.main;

import java.util.ArrayList;
import java.util.List;
import com.tema1.helpers.Constants;

public class Player implements Comparable {
    private int playerindex;
    private ArrayList<Integer> bag;
    private ArrayList<Integer> booth;
    private Constants constanta = new Constants();
    private int coins;
    private int bribe;
    private List<Integer> cardsInHand;
    private int declaration;
    private String tactic;
    private String job;
    private int actualCoins;



    public Player() {
        coins = constanta.START_BUDGET;
        bag = new ArrayList<>();
        booth = new ArrayList<>();
    }

    Player(List<String> playerNames, int i) {
        playerindex = i;
        coins = constanta.START_BUDGET;
        cardsInHand = new ArrayList<Integer>();
        this.tactic = playerNames.get(i);
        this.job = "merchant";
        bag = new ArrayList<>();
        booth = new ArrayList<>();
        bribe = 0;
        actualCoins = coins;
    }

    public final int getPlayerindex() {
        return playerindex;
    }

    public final ArrayList<Integer> getBag() {
        return bag;
    }

    public final ArrayList<Integer> getBooth() {
        return booth;
    }

    public final void setBooth(ArrayList<Integer> booth) {
        this.booth = booth;
    }

    public final int getCoins() {
        return coins;
    }

    public final int getBribe() {
        return bribe;
    }

    public final int getActualCoins() {
        return actualCoins;
    }

    public final void setActualCoins(int actualCoins) {
        this.actualCoins = actualCoins;
    }

    public final List<Integer> getCardsInHand() {
        return cardsInHand;
    }

    public final void setCardsInHand(List<Integer> cards, int index) {
        this.cardsInHand = cards.subList(index * constanta.CARDS_PICK, (index + 1) * constanta.CARDS_PICK);
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

    public final void setBribe(int bribe) {
        this.bribe = bribe;
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

    public void playBasic(ArrayList<Player> jucatori, Player player, List<Integer> cards, int round) {

    }

    public final String toString() {
        return playerindex + "Player{" +
                "bag=" + bag +
                ", booth=" + booth +
                ", coins=" + coins +
                ", cardsInHand=" + cardsInHand +
                ", declaration=" + declaration +
                ", tactic='" + tactic + '\'' +
                '}';
    }


    public final int compareTo(Object o) {
        int compareCoins = ((Player) o).getCoins();
        return compareCoins - this.coins;
    }

//    public boolean checkIfIlegalHand(List<Integer> cardsInHand) {
//        return true;
//    }
//
//    public void getMaxIllegal(List<Integer> cardsInHand) {
//    }
}
