package com.tema1.main;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.helpers.Constants;

import java.util.Collections;
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
        constants = new Constants();
    }

    public Basic(List<Integer> cardsInHand, List<String> PlayerNames, int i) {
        super(cardsInHand, PlayerNames, i);
        this.products = GoodsFactory.getInstance();
        this.frequency = new HashMap<Integer, Integer>();
        constants = new Constants();
    }

    public final void playBasic(Player player) {
        if (player.getJob().equals("merchant")) {
            basicMerchant(player);
        } else {
            basicSheriff(player);
        }
    }

    public final void basicMerchant(Player player) {
        if (!(checkIfLegalHand(player.getCardsInHand()))) {
            getCardMaxProfit(player.getCardsInHand());
        } else {
          getBestItem(player, player.getCardsInHand());
        }
    }

    public void basicSheriff(Player player) {

    }

    public final void getBestItem(Player player, List<Integer> cardsInHand) {
        // pun in map-ul de frecvete id urile itemelor din mana si frecventele cu care apar
        if (checkIfLegal(cardsInHand.get(0))) {
            this.frequency.put(cardsInHand.get(0), 1);
        }
        for (int i = 0; i < cardsInHand.size() ; ++i) {
            if (!cardsInHand.get(i).equals(cardsInHand.get(0)) && checkIfLegal(cardsInHand.get(i))) {
                this.frequency.put(cardsInHand.get(i), 0);
            }
        }
        if (checkIfLegal(cardsInHand.get(0))) {
            this.frequency.put(cardsInHand.get(0), 0);
        }
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (checkIfLegal(cardsInHand.get(i))) {
                this.frequency.put(cardsInHand.get(i), this.frequency.get(cardsInHand.get(i)) + 1);
            }
        }

        System.out.println(frequency.values());
        //gasesc cartea care apare cel mai des
        int maxValueInMap = (Collections.max(this.frequency.values()));
        int key = 0;
        int checker = 0;
        for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                key = entry.getKey();
                checker++;
            }
        }
        // intra in if daca sunt mai multe carti cu frecventa maxima gasita
        if (checker > 1) {
            // profitul minim este 2(mere), iar indexul este cel mai mic posibil(tot al merelor)
            int maxProfit = 2;
            int maxIndex = 0;
            for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
                //pastrez indexul itemului cu profitul cel mai mare
                if (entry.getValue() == maxValueInMap && products.getGoodsById(entry.getKey()).getProfit() > maxProfit) {
                    maxProfit = products.getGoodsById(entry.getKey()).getProfit();
                    maxIndex = entry.getKey();
                    //daca cele 2 carti au aceeasi frecventa(maxima) si acelasi profit(maxim) o alege pe cea cu indexul maxim
                } else if (entry.getValue() == maxValueInMap && products.getGoodsById(entry.getKey()).getProfit() == maxProfit
                        && entry.getKey() > maxIndex) {
                    maxIndex = entry.getKey();
                }
            }

        }
        // pun cartile/cartea cele mai bune pt jucatorul basic
        for (int i = 0; i < maxValueInMap; ++i) {
            player.getBag().add(key);
        }
        setDeclaration(key);
    }

    // urmatoarea metoda verifica daca in mana exista carti legale pe care basic le puate lua
    public final boolean checkIfLegalHand(List<Integer> cardsInHand) {
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getType() == GoodsType.Legal) {
                return true;
            }
        }
        return false;
    }

    //urmatoarea metoda verifica daca o carte este legala
    public final boolean checkIfLegal(int index) {
        if (products.getGoodsById(index).getType() == GoodsType.Legal) {
            return true;
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
        setDeclaration(0);
        // adauga cartea ilegala cu profitul cel mai mare in sac si o declara ca fiind mere
    }

}
