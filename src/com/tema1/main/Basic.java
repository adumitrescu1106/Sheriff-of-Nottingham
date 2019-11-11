package com.tema1.main;

import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.helpers.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Basic extends Player {
    private Constants constants;
    private GoodsFactory products;
    private Map<Integer, Integer> frequency;
    private ArrayList<Integer> usedCards;


    public Basic() {
        super();
        this.products = GoodsFactory.getInstance();
        this.frequency = new HashMap<Integer, Integer>();
        constants = new Constants();
    }

    public Basic(List<String> playerNames, int i) {
        super(playerNames, i);
        this.products = GoodsFactory.getInstance();
        this.frequency = new HashMap<Integer, Integer>();
        constants = new Constants();
        usedCards = new ArrayList<Integer>();
        for (int j = 0; j < constants.CARDS_PICK; ++j) {
            usedCards.add(0);
        }
    }

    public final void playBasic(ArrayList<Player> jucatori, Player player, List<Integer> cards, int round) {
        frequency.clear();
        usedCards.clear();
        for (int j = 0; j < constants.CARDS_PICK; ++j) {
            usedCards.add(0);
        }
        if (player.getTactic().equals("basic") || player.getTactic().equals("bribed")) {
            if (player.getJob().equals("merchant")) {
                basicMerchant(player);
            } else {
                basicSheriff(jucatori, player, cards);
            }
        }
        if (player.getTactic().equals("greedy")) {
            if (player.getJob().equals("merchant")) {
               basicMerchant(player);
                if (round % 2 == 0 && checkIfIlegalHand(player.getCardsInHand())) {
                  getCardMaxProfit(player, player.getCardsInHand());
                    //System.out.println("intra par");
                }
            } else {
                basicSheriff(jucatori, player, cards);
            }
        }
    }


    public final void basicMerchant(Player player) {
        if (!(checkIfLegalHand(player.getCardsInHand()))) {
            // adauga cartea ilegala cu profitul cel mai mare in sac si o declara ca fiind mere
            getCardMaxProfit(player, player.getCardsInHand());
            setDeclaration(0);
            //System.out.println("intra aici");
        } else {
          getBestItem(player, player.getCardsInHand());
        }
    }

    public final void basicSheriff(ArrayList<Player> jucatori, Player sheriff, List<Integer> cards) {
        // Daca sheriful are o suma mai mare sau egala cu 16 , atunci controleaza jucatorii
        boolean inRegula;
        if (sheriff.getCoins() >= constants.SARACIE) {
            for (int i = 0; i < jucatori.size(); ++i) {
                inRegula = true;
                // verific restul jucatorilor (mai putin sherifful)
                if (jucatori.get(i).getJob().equals("merchant")) {
                   for (int j = 0; j < jucatori.get(i).getBag().size(); j++) {
                       // daca sunt carti ilegale in posesia jucatorilor , seriful le confisca si aplica pedeapsa
                       if (products.getGoodsById(jucatori.get(i).getBag().get(j)).getType().equals(GoodsType.Illegal)) {
                           inRegula = false;
                       } else if (jucatori.get(i).getBag().get(j) != jucatori.get(i).getDeclaration()) {
                           inRegula = false;
                           //sherifful castiga banii pt ca a prins obiecte nedeclarate, iar pietarul pierde banii
                       }
                   }
                   // daca comerciantul este in regula , atunci sherifful pierde banii
                   if (inRegula) {
                        sheriff.subCoins(products.getGoodsById(jucatori.get(i).getDeclaration()).getPenalty() * jucatori.get(i).getBag().size());
                        jucatori.get(i).addCoins(products.getGoodsById(jucatori.get(i).getDeclaration()).getPenalty() * jucatori.get(i).getBag().size());
//                        System.out.println(jucatori.get(i).getCoins());
//                        System.out.println("sheriff " + sheriff.getCoins());
                        for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                            jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                        }
                   } else {
                       for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                           if (jucatori.get(i).getBag().get(k) == jucatori.get(i).getDeclaration()) {
                               jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                               //jucatori.get(i).addCoins(products.getGoodsById(jucatori.get(i).getBag().get(k)).getProfit());
                           } else {
                               jucatori.get(i).subCoins(products.getGoodsById(jucatori.get(i).getBag().get(k)).getPenalty());
                               sheriff.addCoins(products.getGoodsById(jucatori.get(i).getBag().get(k)).getPenalty());
                               //confiscare
                           }
                       }
                   }
                }
            }
        }
    }

    public final void getBestItem(Player player, List<Integer> cardsInHand) {
        // pun in map-ul de frecvete id urile itemelor din mana si frecventele cu care apar
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (checkIfLegal(cardsInHand.get(i))) {
                this.frequency.put(cardsInHand.get(i), 0);
            }
        }
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (checkIfLegal(cardsInHand.get(i))) {
                this.frequency.put(cardsInHand.get(i), this.frequency.get(cardsInHand.get(i)) + 1);
            }
        }

        //gasesc cartea care apare cel mai des
        int maxValueInMap = (Collections.max(this.frequency.values()));
        int key = 0;
        for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                key = entry.getKey();
                break;
            }
        }
        int checker = 0;
        for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
            if (entry.getValue() == maxValueInMap && key != entry.getKey()) {
                checker++;
            }
        }
        // intra in if daca sunt mai multe carti cu frecventa maxima gasita
        if (checker > 0) {
            // profitul minim este 2(mere), iar indexul este cel mai mic posibil(tot al merelor)
            int maxProfit = 2;
            int maxIndex = 0;
            for (Map.Entry<Integer, Integer> entry : this.frequency.entrySet()) {
                //pastrez indexul itemului cu profitul cel mai mare
                if (entry.getValue() == maxValueInMap && products.getGoodsById(entry.getKey()).getProfit() > maxProfit) {
                    maxProfit = products.getGoodsById(entry.getKey()).getProfit();
                    maxIndex = entry.getKey();
                    key = entry.getKey();
                    //daca cele 2 carti au aceeasi frecventa(maxima) si acelasi profit(maxim) o alege pe cea cu indexul maxim
                } else if (entry.getValue() == maxValueInMap && products.getGoodsById(entry.getKey()).getProfit() == maxProfit
                        && entry.getKey() > maxIndex) {
                    maxIndex = entry.getKey();
                    key = entry.getKey();
                }
            }
        }
        // pun cartile/cartea cele mai bune pt jucatorul basic
        for (int i = 0; i < maxValueInMap; ++i) {
            player.getBag().add(key);
        }

        //setez indexul pentru cartile folosite
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (cardsInHand.get(i) == key) {
                usedCards.set(key, 1);
            }
        }
        //stabilesc ce declara ca are in sac si ii dau profitul pe produsele din sac pt ca spune adevarul in cazul asta
        setDeclaration(key);
        //addCoins(maxValueInMap * products.getGoodsById(key).getProfit());
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

    // urmatoarea metoda verifica daca in mana exista carti ilegale
    public final boolean checkIfIlegalHand(List<Integer> cardsInHand) {
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getType() == GoodsType.Illegal && usedCards.get(i) == 0) {
                return true;
            }
        }
        return false;
    }


    // adauga cartea ilegala cu profitul cel mai mare in sac
    public final void getCardMaxProfit(Player player, List<Integer> cardsInHand) {
        int max = 0;
        int index = 0;
        for (int i = 1; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getProfit() > products.getGoodsById(max).getProfit()
            && usedCards.get(i) == 0) {
                max = cardsInHand.get(i);
                index = 1;
            }
        }
        //pun cartea in sac si ii setez indexul pt ca a fost folosita
        player.getBag().add(max);
        usedCards.set(index, 1);
    }

    //metoda urmatoare pune in sac cartea ilegala cu profitul maxim
    public final void getMaxIllegal(List<Integer> cardsInHand) {
        int max = -1;
        int index = 0;
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getType().equals(GoodsType.Illegal) && usedCards.get(i) == 0) {
                max = cardsInHand.get(i);
                break;
            }
        }
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getType().equals(GoodsType.Illegal) &&
                    products.getGoodsById(cardsInHand.get(i)).getProfit() > products.getGoodsById(max).getProfit()
            && usedCards.get(i) == 0) {
                max = cardsInHand.get(i);
                index = i;
            }
        }
        this.getBag().add(max);
        usedCards.set(index, 1);
    }


    //metoda urmatoare reprezinta confiscarea cartii cu indexul j
    public final void confiscate(ArrayList<Integer> bag, List<Integer> cards, int index) {
        //cards.add(bag.get(index));
        bag.remove(index);
    }

}
