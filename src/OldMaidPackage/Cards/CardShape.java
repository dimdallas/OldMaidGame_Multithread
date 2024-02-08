package Cards;

public enum CardShape {
    SPADE("♠", CardColor.BLACK),
    CLUB("♣", CardColor.BLACK),
    HEART("♥", CardColor.RED),
    DIAMOND("♦", CardColor.RED);

    private final String symbol;
    private final CardColor color;

    CardShape(String symbol, CardColor color) {
        this.symbol = symbol;
        this.color = color;
    }

    public CardColor getColor() {
        return color;
    }

    public String toString(){
        return color.getAnsiColor() + symbol + CardColor.ANSI_RESET;
    }
}
