package GameUtilities;

import PlayerUtilities.Player;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayersQueue {
    private final List<Player> players;
    private Player currentPlayer;
    private static PlayersQueue Instance;

    public static PlayersQueue getInstance() {
        if(Instance == null){
            Instance = new PlayersQueue();
        }
        return Instance;
    }
    private PlayersQueue() {
        players = new ArrayList<>();
    }

    // PLAYERS LIST METHODS
    public void createPlayers(int number, Scanner sc){
        Lock lock = new ReentrantLock();
        CountDownLatch countDownLatch = new CountDownLatch(number);

        for(int i =0; i < number; i++) {
            System.out.print("PlayerUtilities.Player name: ");
            Player newPlayer = new Player(sc.nextLine(), lock, countDownLatch);
            players.add(newPlayer);
        }
    }

    public void createStaticPlayers(int number){
        Lock lock = new ReentrantLock();
        CountDownLatch countDownLatch = new CountDownLatch(number);

        players.add(new Player("111111", lock, countDownLatch));
        players.add(new Player("222222", lock, countDownLatch));

        if (number > 2)
            players.add(new Player("333333", lock, countDownLatch));
        if (number > 3)
            players.add(new Player("444444", lock, countDownLatch));
        if (number > 4)
            players.add(new Player("555555", lock, countDownLatch));
        if (number > 5)
            players.add(new Player("666666", lock, countDownLatch));
    }

    public List<Player> getPlayersListCopy(){
        return new ArrayList<>(players);
    }

    public List<Player> getPlayersListReference(){
        return players;
    }

    public List<Player> getPlayersListView(){
        return Collections.unmodifiableList(players);
    }

    public int remainingPlayers() {
        return players.size();
    }

    public boolean isPlayerAlone() {
        return remainingPlayers() == 1;
    }

    public void removeFromQueue(Player player) {
        if(playerIsCurrent(player))
            setNextPlayer();

        players.remove(player);
    }


    // PLAYERS ORDER METHODS
    private Player findFirstPlayer() {     // 1st PlayerUtilities.Player in order with minimum cards
        if(players.stream()
                .min(Comparator.comparing(Player::cardsRemaining))
                .isPresent()) {
            return players.stream()
                    .min(Comparator.comparing(Player::cardsRemaining))
                    .get();
        }
        return null;
    }

    public Player setFirstPlayer() {
        Player firstPlayer = findFirstPlayer();
        if (firstPlayer == null)
            throw new RuntimeException("No 1st player decided");
//        setCurrentPlayerIndex(players.indexOf(firstPlayer));
        setCurrentPlayer(firstPlayer);
        return firstPlayer;
    }

    // PLAYER LIST SEARCHING METHODS
    public boolean playerIsCurrent(Player player) {
        return player.equals(getCurrentPlayer());
    }
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public Player getNextPlayer(){
        if (currentPlayer.equals(getLastPlayer()))
            return getFirstPlayer();
        return players.get(getCurrentPlayerIndex() + 1);
    }

    public void setNextPlayer() {
        currentPlayer = getNextPlayer();
    }

    public Player getPreviousPlayer(){
        if (currentPlayer.equals(getFirstPlayer()))
            return getLastPlayer();
        return players.get(getCurrentPlayerIndex() - 1);
    }

    private Player getFirstPlayer() {
        return players.get(0);
    }

    private Player getLastPlayer() {
        return players.get(lastPlayerIndex());
    }
    // PLAYER INDEX METHODS

    private int getCurrentPlayerIndex(){
        return players.indexOf(currentPlayer);
    }

    public int lastPlayerIndex() {
        return remainingPlayers() - 1;
    }

    public int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public Player getPlayerFromIndex(int index) {
        return players.get(index);
    }
}
