package PlayerUtilities;

import Cards.Card;
import Cards.CardValue;
import GameUtilities.PlayersQueue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public class Player extends Thread{
    private final String name;
    private final Lock takeTurn;
    private final CountDownLatch groupSync;

    private final PlayersQueue playersQueue;
    private final CardsInHand cards;
    private final Random rng;

    private boolean win = false;

    public Player(String name, Lock lock, CountDownLatch latch){
        this.name = name;
        takeTurn = lock;
        groupSync = latch;

        playersQueue = PlayersQueue.getInstance();

        cards = new CardsInHand();
        rng = new Random();
    }

    //PLAYER STATE METHODS
    private void setWinner(){
        win = true;
    }

    public boolean isWinner(){
        return win;
    }

    //PLAYER CARDS METHODS
    private boolean hasOnlyJoker(){
        return cards.remainingCards() == 1 && cards.contains(CardValue.JOKER);
    }
    private boolean checkEmptyHand(){
        return cards.noCardsRemaining();
    }

    public int cardsRemaining(){
        return cards.remainingCards();
    }

    private void addCard(Card card) {
        cards.add(card);
    }

    public void dealCard(Card card) {
        cards.add(card);
    }

    public Card removeRandomCard(){
        return cards.removeCard(rng.nextInt(cards.remainingCards()));
    }

    private List<Card> tryPlayCardPair(){
        if (cards.pairsExist()) {
            return cards.discardPairs();
        }
        return null;
    }

    public String cardsString(List<Card> cardList){
        return CardListPrinter.stringify(cardList);
    }

    //PLAYER QUEUE METHODS
    private boolean lastWithJoker() {
        return playersQueue.isPlayerAlone() && hasOnlyJoker();
    }

    private boolean notMyTurn() {
        return !playersQueue.isPlayerAlone() && !playersQueue.playerIsCurrent(this);
    }

    private void leaveGame() {
        playersQueue.removeFromQueue(this);
        takeTurn.notifyAll();
    }

    private void compactSleep(long l){
        try {
            sleep(l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
        super.run();

        compactSleep(500);

        // FREE FOR ALL PART, ASYNCHRONOUS
        System.out.println(name + " has initial cards: " + cards.remainingCards() + "\n" + cards);
        List<Card> initialPairs = tryPlayCardPair();
        if(initialPairs != null)
            System.out.println(name + " discards " + cardsString(initialPairs));
        else
            System.out.println(name + " PASS");


        groupSync.countDown();
        try {
            System.out.println(name + "------WAITS OTHER PLAYERS------");
            groupSync.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("-----------------------------");
        while (!checkEmptyHand()) {
            synchronized (takeTurn) {
                while (notMyTurn()) {
                    try {
                        System.out.println(name + " waiting for turn");
                        takeTurn.wait();
                        System.out.println("-----------------------------");
                        System.out.println(name + " wakes while turn is for " + playersQueue.getCurrentPlayer());
                        System.out.println("remaining players: " + playersQueue.remainingPlayers());
                        if (checkEmptyHand()) {
                            setWinner();

                            System.out.println(name + " last card was taken, leaves game");
                            leaveGame();
                            return;
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


                while (!playersQueue.isPlayerAlone()) {
                    Player previousPlayer = playersQueue.getPreviousPlayer();
                    if(previousPlayer.checkEmptyHand()) {
                        System.out.println("PlayerUtilities.Player " + previousPlayer + " no cards, wait until he leaves");
                        takeTurn.notifyAll();
                        try {
                            takeTurn.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        Card takenCard = previousPlayer.removeRandomCard();
                        System.out.println(name + " takes " + takenCard + " from " + previousPlayer);
                        addCard(takenCard);
                        break;
                    }
                }

                System.out.println(name + " cards remaining: " + cards.remainingCards());
                System.out.println(name + " " + cards);

                List<Card> discarded = tryPlayCardPair();
                if(discarded != null)
                    System.out.println(name + " discards " + cardsString(discarded));
                else
                    System.out.println(name + " PASS");

                if (checkEmptyHand()) {
                    setWinner();

                    System.out.println(name + " discarded all cards, leaves game");
                    leaveGame();
                    return;
                } else if (lastWithJoker()) {
                    System.out.println(name + " was left with JOKER, leaves game");
                    leaveGame();

                    // break exits synchronized, not while
                    return;
                }

                if(!playersQueue.isPlayerAlone()) {
                    playersQueue.setNextPlayer();
                }

//                compactSleep(200);
                takeTurn.notifyAll();
            }
        }
    }
}
