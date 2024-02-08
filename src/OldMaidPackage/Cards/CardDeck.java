package Cards;
import java.util.*;

public class CardDeck implements Iterable<Card>{
    List<Card> cards;

    public CardDeck() {
        cards = new LinkedList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for(CardShape shape : CardShape.values()){
            for(CardValue value : CardValue.values()){
                if(value == CardValue.JOKER)
                    continue;
                cards.add(Card.createCard(value, shape));
            }
        }
        cards.add(Card.createCard(CardValue.JOKER, CardShape.DIAMOND));
        shuffleDeck();
    }

    public void shuffleDeck(){
        Collections.shuffle(cards);
    }

    public Card removeCard() throws NoSuchElementException{
        if(cards.isEmpty())
            throw new NoSuchElementException("No cards left on this deck");
        return cards.remove(0);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public boolean isNotEmpty() {
        return !cards.isEmpty();
    }
}
