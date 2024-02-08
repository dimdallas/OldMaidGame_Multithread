package PlayerUtilities;

import Cards.Card;
import Cards.CardValue;

import java.util.*;

public class CardsInHand implements Iterable<Card>{
    private final List<Card> cardList;
    private final List<Card> pairCards;

    public CardsInHand() {
        cardList = new LinkedList<>();
        pairCards = new LinkedList<>();
    }

    public boolean noCardsRemaining() {
        return cardList.isEmpty();
    }

    public int remainingCards(){
        return cardList.size();
    }

    public void add(Card card) {
        for(Card cardInHand : cardList){
            if(cardInHand.isPair(card)){
                pairCards.add(cardInHand);
                pairCards.add(card);
            }
        }
        cardList.add(card);
    }

    public boolean contains(CardValue cardValue) {
        for(Card card : cardList){
            if(card.getCardValue() == cardValue)
                return true;
        }
        return false;
    }

    public Card removeCard(int index) {
        return cardList.remove(index);
    }

    public List<Card> getAll() {
        return Collections.unmodifiableList(cardList);
    }

    public boolean pairsExist() {
        return !pairCards.isEmpty();
    }

    public List<Card> discardPairs(){
        List<Card> pair = new LinkedList<>(pairCards);

        cardList.removeAll(pairCards);
        pairCards.clear();
        return pair;
    }

    public List<Card> getPairs() {
        return Collections.unmodifiableList(pairCards);
    }

    private void sort(){
        cardList.sort((card1, card2) -> {
            if(card1.getCardValue() == card2.getCardValue())
                return card1.getCardShape().compareTo(card2.getCardShape());
            return card1.getCardValue().compareTo(card2.getCardValue());
        });
    }

    @Override
    public String toString() {
        sort();
        return CardListPrinter.stringify(cardList);
    }

    @Override
    public Iterator<Card> iterator() {
        return cardList.iterator();
    }
}
