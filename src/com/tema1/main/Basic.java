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
    private ArrayList<Integer> merchantsToCheck;


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
        merchantsToCheck = new ArrayList<Integer>();
    }

    public final void playBasic(ArrayList<Player> jucatori, Player player,
                                List<Integer> cards, int round) {
        clearEachPlay();
        player.setActualCoins(player.getCoins());
        //basic player
        if (player.getTactic().equals("basic")) {

            if (player.getJob().equals("merchant")) {
                basicMerchant(player);
            } else {
                setCheckBasic(jucatori.size());
                basicSheriff(jucatori, player, cards);
            }
        }
        //greedy player
        if (player.getTactic().equals("greedy")) {
            if (player.getJob().equals("merchant")) {
               basicMerchant(player);
                if (round % 2 == 0 && checkIfIlegalHand(player.getCardsInHand())) {
                  getCardMaxProfit(player, player.getCardsInHand());
                    //System.out.println("intra par");
                }
            } else {
                setCheckBasic(jucatori.size());
                basicSheriff(jucatori, player, cards);
            }
        }
        //briber player
        if (player.getTactic().equals("bribed")) {
            if (player.getJob().equals("merchant")) {
                //pietar bribed
                //daca nu are nicio carte ilegala in mana sau daca nu are bani de bribe, joaca basic
                if (player.getActualCoins() < constants.MIN_BRIBE || !checkIfIlegalHand(player.getCardsInHand())) {
                    basicMerchant(player);
                } else {
                    bribedMerchant(player);
                }
            } else {
                //sheriff bribed
                setCheckBribed(jucatori, player.getPlayerindex());
                basicSheriff(jucatori, player, cards);
            }
        }
    }

    //metoda pentru merchant ul cu stilul de joc basic
    public final void basicMerchant(Player player) {
        if (!(checkIfLegalHand(player.getCardsInHand()))) {
            // adauga cartea ilegala cu profitul cel mai mare in sac si o declara ca fiind mere
            getCardMaxProfit(player, player.getCardsInHand());
            setDeclaration(0);
        } else {
          getBestItem(player, player.getCardsInHand());
        }
    }

    //metoda pentru sheriff ul cu stilul de joc basic
    public final void basicSheriff(ArrayList<Player> jucatori,
                                   Player sheriff, List<Integer> cards) {
        boolean inRegula;
        if (sheriff.getCoins() >= constants.SARACIE) {
            //System.out.println(sheriff.getCoins());
            if (sheriff.getTactic().equals("greedy")) {
                for (int i = 0; i < jucatori.size(); ++i) {
                    inRegula = true;
                    if (jucatori.get(i).getJob().equals("merchant")) {
                        if (jucatori.get(i).getBribe() != 0) {
                            sheriff.addCoins(jucatori.get(i).getBribe());
                            jucatori.get(i).subCoins(jucatori.get(i).getBribe());
                            jucatori.get(i).setBribe(0);
                            for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                                jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                            }
                        } else {
                            for (int j = 0; j < jucatori.get(i).getBag().size(); j++) {
                                if (products.getGoodsById(jucatori.get(i).getBag().get(j)).getType().
                                        equals(GoodsType.Illegal) && jucatori.get(i).getBag().get(j)
                                        != jucatori.get(i).getDeclaration()) {
                                    inRegula = false;
                                }
                            }
                            if (inRegula) {
                                sheriff.subCoins(products.getGoodsById(
                                        jucatori.get(i).getDeclaration()).getPenalty()
                                        * jucatori.get(i).getBag().size());
                                jucatori.get(i).addCoins(products.getGoodsById(
                                        jucatori.get(i).getDeclaration()).getPenalty()
                                        * jucatori.get(i).getBag().size());
//                        System.out.println(jucatori.get(i).getCoins());
//                        System.out.println("sheriff " + sheriff.getCoins());
                                for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                                    jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                                }
                            } else {
                                for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                                    if (jucatori.get(i).getBag().get(k)
                                            == jucatori.get(i).getDeclaration()) {
                                        jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                                    } else {
                                        jucatori.get(i).subCoins(products.getGoodsById(
                                                jucatori.get(i).getBag().get(k)).getPenalty());
                                        sheriff.addCoins(products.getGoodsById(
                                                jucatori.get(i).getBag().get(k)).getPenalty());
                                        //confiscare
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Daca sheriful are o suma mai mare sau egala cu 16 , atunci controleaza jucatorii
                if (sheriff.getCoins() >= constants.SARACIE) {
                    for (int i = 0; i < jucatori.size(); ++i) {
                        inRegula = true;
                        // verific restul jucatorilor (mai putin sherifful)
                        if (jucatori.get(i).getJob().equals("merchant") && indexToCheck(i)) {
                            for (int j = 0; j < jucatori.get(i).getBag().size(); j++) {
                                // daca sunt carti ilegale in posesia jucatorilor
                                // seriful le confisca si aplica pedeapsa
                                if (products.getGoodsById(jucatori.get(i).getBag().get(j)).getType().
                                        equals(GoodsType.Illegal)) {
                                    inRegula = false;
                                } else if (jucatori.get(i).getBag().get(j)
                                        != jucatori.get(i).getDeclaration()) {
                                    inRegula = false;
                                    //sherifful castiga banii pt ca a prins obiecte nedeclarate,
                                    // iar pietarul pierde banii
                                }
                            }
                            // daca comerciantul este in regula , atunci sherifful pierde banii
                            if (inRegula) {
                                sheriff.subCoins(products.getGoodsById(
                                        jucatori.get(i).getDeclaration()).getPenalty()
                                        * jucatori.get(i).getBag().size());
                                jucatori.get(i).addCoins(products.getGoodsById(
                                        jucatori.get(i).getDeclaration()).getPenalty()
                                        * jucatori.get(i).getBag().size());
//                        System.out.println(jucatori.get(i).getCoins());
//                        System.out.println("sheriff " + sheriff.getCoins());
                                for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                                    jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                                }
                            } else {
                                for (int k = 0; k < jucatori.get(i).getBag().size(); k++) {
                                    if (jucatori.get(i).getBag().get(k)
                                            == jucatori.get(i).getDeclaration()) {
                                        jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(k));
                                    } else {
                                        jucatori.get(i).subCoins(products.getGoodsById(
                                                jucatori.get(i).getBag().get(k)).getPenalty());
                                        sheriff.addCoins(products.getGoodsById(
                                                jucatori.get(i).getBag().get(k)).getPenalty());
                                        //confiscare
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < jucatori.size(); ++i) {
                if (i != sheriff.getPlayerindex()) {
                    for (int j = 0; j < jucatori.get(i).getBag().size(); j++) {
                        jucatori.get(i).getBooth().add(jucatori.get(i).getBag().get(j));
                    }
                }
            }
        }
    }
    // urmatoarea metoda curata structurile de date care se refolosesc in fiecare runda
    public final void clearEachPlay() {
        frequency.clear();
        usedCards.clear();
        merchantsToCheck.clear();
        for (int j = 0; j < constants.CARDS_PICK; ++j) {
            usedCards.add(0);
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
                if (entry.getValue() == maxValueInMap
                        && products.getGoodsById(entry.getKey()).getProfit() > maxProfit) {
                    maxProfit = products.getGoodsById(entry.getKey()).getProfit();
                    maxIndex = entry.getKey();
                    key = entry.getKey();
                    //daca cele 2 carti au aceeasi frecventa(maxima) si a
                    // celasi profit(maxim) o alege pe cea cu indexul maxim
                } else if (entry.getValue() == maxValueInMap
                        && products.getGoodsById(entry.getKey()).getProfit() == maxProfit
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
        //stabilesc ce declara ca are in sac si ii dau profitul pe
        // produsele din sac pt ca spune adevarul in cazul asta
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
            if (products.getGoodsById(cardsInHand.get(i)).getType()
                    == GoodsType.Illegal && usedCards.get(i) == 0) {
                return true;
            }
        }
        return false;
    }


    // adauga cartea ilegala cu profitul cel mai mare in sac
    public final void getCardMaxProfit(Player player, List<Integer> cardsInHand) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getProfit()
                    > products.getGoodsById(max).getProfit()
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
            if (products.getGoodsById(cardsInHand.get(i)).getType().equals(GoodsType.Illegal)
                    && usedCards.get(i) == 0) {
                max = cardsInHand.get(i);
                break;
            }
        }
        for (int i = 0; i < cardsInHand.size(); ++i) {
            if (products.getGoodsById(cardsInHand.get(i)).getType().equals(GoodsType.Illegal)
                    && products.getGoodsById(cardsInHand.get(i)).getProfit()
                            > products.getGoodsById(max).getProfit()
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

    //Bribed
    public final void bribedMerchant(Player player) {
        List<Integer> legalItems = new ArrayList<>();
        List<Integer> illegalItems = new ArrayList<>();
        int nrCartiIlegale = 0;
        for (int i = 0; i < player.getCardsInHand().size(); ++i) {
            if (checkIfLegal(player.getCardsInHand().get(i))) {
                legalItems.add(player.getCardsInHand().get(i));
            } else {
                illegalItems.add(player.getCardsInHand().get(i));
            }
        }
        //separ itemele legale de cele ilegale si le sortez dupa profit
        legalItems.sort(this::compareProfits);
        illegalItems.sort(this::compareProfits);
        if (0 < player.getActualCoins()) {
            for (int i = 0; i < illegalItems.size(); i++) {
                //daca are bani si inca nu are 8 iteme
                if (player.getActualCoins() - constants.ILLEGAL_PENALTY > 0 && player.getBag().size() < constants.BAG_SIZE) {
                    player.getBag().add(illegalItems.get(i));
                    player.setActualCoins(player.getActualCoins() - constants.ILLEGAL_PENALTY);
                    nrCartiIlegale++;
                } else {
                    //daca nu mai are bani nu mai ia iteme
                    break;
                }
            }
            for (int i = 0; i < legalItems.size(); ++i) {
                if (player.getActualCoins() - constants.LEGAL_PENALTY > 0 && player.getBag().size() < constants.BAG_SIZE) {
                    player.getBag().add(legalItems.get(i));
                    player.setActualCoins(player.getActualCoins() - constants.LEGAL_PENALTY);
                } else {
                    break;
                }
            }
        }
        //daca are mai mult de 2 carti ilegale seteaza bribeul 10
        if (nrCartiIlegale > 2) {
            player.setBribe(constants.MAX_BRIBE);
        }
        //daca are 1 sau 2 carti ilegale seteaza bribeul 5
        if (nrCartiIlegale == 1 || nrCartiIlegale == 2) {
            player.setBribe(constants.MIN_BRIBE);
        }
        //orice ar avea in sac declara mere
        if (player.getBag().size() > 0) {
            player.setDeclaration(0);
        }

    }

    //urmatoarea metoda verifica daca jucatorul cu indexul transmis trebuie verificat
    public final boolean indexToCheck(int indexPlayer) {
        for (int i = 0; i < merchantsToCheck.size(); ++i) {
            if (merchantsToCheck.get(i) == indexPlayer) {
                return true;
            }
        }
        return false;
    }

    //seteaza comerciantii care trebuie verificati in cazul sheriffului basic
    public final void setCheckBasic(int noPlayers) {
        for (int i = 0; i < noPlayers; ++i) {
            merchantsToCheck.add(i);
        }
    }

    //seteaza comerciantii care trebuie verificati in cazul sheriffului bribed
    public final void setCheckBribed(List<Player> jucatori, int sheriffIndex) {
        if (jucatori.size() > 2) {
            if (sheriffIndex == (jucatori.size() - 1)) {
                merchantsToCheck.add(0);
                merchantsToCheck.add(sheriffIndex - 1);
            } else if (sheriffIndex == 0) {
                merchantsToCheck.add(jucatori.size() - 1);
                merchantsToCheck.add(1);
            } else {
                merchantsToCheck.add(sheriffIndex - 1);
                merchantsToCheck.add(sheriffIndex + 1);
            }
        } else {
            if (sheriffIndex == 1) {
                merchantsToCheck.add(0);
            } else {
                merchantsToCheck.add(1);
            }
        }
    }

    //comparator cu care sortez itemele de la profitul cel mai mare la cel mai mic
    public final int compareProfits(int one, int two) {
        if (products.getGoodsById(two).getProfit() == products.getGoodsById(one).getProfit()) {
            return two - one;
        }
        return products.getGoodsById(two).getProfit() - products.getGoodsById(one).getProfit();
    }

}
