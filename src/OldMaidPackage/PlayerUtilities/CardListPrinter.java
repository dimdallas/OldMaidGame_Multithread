package PlayerUtilities;

import Cards.Card;

import java.util.List;

public class CardListPrinter {
    public static String stringify(List<Card> cards){
        StringBuilder builder = new StringBuilder();
        for (Card card : cards){
            builder.append(card).append(" | ");
        }

        return builder.toString();
    }
}
