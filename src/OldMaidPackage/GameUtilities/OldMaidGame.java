package GameUtilities;

import Cards.CardDeck;
import PlayerUtilities.Player;

import java.util.*;

public class OldMaidGame implements Game {
    CardDeck deck;
    List<Player> players;
    PlayersQueue playersQueue;

    public OldMaidGame() {
        deck = new CardDeck();
        playersQueue = PlayersQueue.getInstance();
    }

    private void addStaticPlayers(int number) {
        playersQueue.createStaticPlayers(number);
        players = playersQueue.getPlayersListCopy();
    }

    private void addPlayers() {
        Scanner sc = new Scanner(System.in);
        System.out.print("How many players will play? ");
        int number = new Integer(sc.nextLine());

        System.out.println();

        playersQueue.createPlayers(number, sc);

        players = playersQueue.getPlayersListCopy();
    }

    public void play(){
        addStaticPlayers(6);
//        addPlayers();

        System.out.println("*************DEALING CARDS*************");
        while (deck.isNotEmpty()){
            System.out.println(".");
            for(Player player : players){
                try{
                    player.dealCard(deck.removeCard());
                }
                catch (NoSuchElementException e){
                    break;
                }
            }
        }

        System.out.println("\n*************FIND 1st PLAYER*************");
        Player firstPlayer = playersQueue.setFirstPlayer();
        System.out.println(firstPlayer + " plays 1st");

        System.out.println("\n*************PLAYERS START*************");
        for (Player player : players){
            System.out.println(player + " starts");
            player.start();
        }

        System.out.println("\n*************MAIN WAITS*************");
        for (Player player : players){
            try {
                player.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // PRINT GAME INFO
        System.out.println("\n*************GAME RESULTS*************");
        for (Player player : players){
            if(player.isWinner())
                System.out.println(player + " won the game");
            else
                System.out.println(player + " lost the game");
        }
    }
}
