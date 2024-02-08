package Cards;

public class Card {
    private final CardValue cardValue;
    private final CardShape cardShape;
    private final CardColor cardColor;

    private Card(CardValue cardValue, CardShape cardShape) {
        this.cardValue = cardValue;
        this.cardShape = cardShape;
        this.cardColor = cardShape.getColor();
    }

    public static Card createCard(CardValue cardValue, CardShape cardShape) {
        return new Card(cardValue, cardShape);
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public CardShape getCardShape() {
        return cardShape;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public boolean isPair(Card card) {
        return cardValue == card.getCardValue() && cardColor == card.getCardColor();
    }

    @Override
    public String toString() {
        return cardValue + " " + cardShape;
    }
}
