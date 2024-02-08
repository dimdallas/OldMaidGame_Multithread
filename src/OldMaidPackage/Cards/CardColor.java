package Cards;

public enum CardColor {
    BLACK("\u001B[30m"), RED("\u001B[31m");

    private final String ansiColor;
    public static final String ANSI_RESET = "\u001B[0m";


    CardColor(String ansiColor) {
        this.ansiColor = ansiColor;
    }

    public String getAnsiColor(){
        return ansiColor;
    }
}
