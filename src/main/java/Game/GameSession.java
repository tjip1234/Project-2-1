package Game;

import Game.Cards.Card;
import Game.Cards.Deck;
import Game.Bots.Bot;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameSession implements Cloneable {
    public Player[] players;
    public ArrayList<Card> Table = new ArrayList<>();
    public int currentPlayer;
    int startPlayer = 0;
    int winnerChickenDinner = 0;

    private final ArrayList<BiConsumer<Integer, Integer>> onNextPlayer = new ArrayList<>();
    private final ArrayList<Consumer<Integer>> onTrickComplete = new ArrayList<>();

    private final HashSet<Card> playedCards = new HashSet<>();

    public final boolean isTeamGame;

    public Deck deck;

    private int totalCycles;

    public GameSession(int seed, Player... players) {
        for (var player : players) {
            if (player instanceof Bot bot)
                bot.simulationSession = this::clone;
        }
        if (players.length == 0)
            throw new UnsupportedOperationException("Games don't work like that. Dumbss.");
        if (players.length == 1)
            throw new UnsupportedOperationException(
                    "Trying to play this game by yourself? How sad... Go get some friends");

        if (players.length == 5)
            throw new UnsupportedOperationException("Briscola Chiamata is unsupported.");

        if (players.length > 6)
            throw new UnsupportedOperationException(
                    "As fun as that may sound, you can't play briscola with that many players. The limit is 6.");

        currentPlayer = 0;

        this.players = players;

        isTeamGame = players.length != 2 && players.length % 2 == 0;

        if (players.length == 3)
            deck = new Deck(39, seed);
        else if (players.length == 6)
            deck = new Deck(36, seed);
        else
            deck = new Deck(40, seed);

        totalCycles = deck.getSessionCards().size()/ players.length;
    }

    public GameSession(Player... players) {


        if (players.length == 0)
            throw new UnsupportedOperationException("Games don't work like that. Dumbss.");
        if (players.length == 1)
            throw new UnsupportedOperationException(
                    "Trying to play this game by yourself? How sad... Go get some friends");

        if (players.length == 5)
            throw new UnsupportedOperationException("Briscola Chiamata is unsupported.");

        if (players.length > 6)
            throw new UnsupportedOperationException(
                    "As fun as that may sound, you can't play briscola with that many players. The limit is 6.");

        currentPlayer = 0;

        this.players = players;

        isTeamGame = players.length != 2 && players.length % 2 == 0;

        if (players.length == 3)
            deck = new Deck(39);
        else if (players.length == 6)
            deck = new Deck(36);
        else
            deck = new Deck(40);

        totalCycles = deck.getSessionCards().size()/ players.length;

        for (var player : players) {
            if (player instanceof Bot bot) {
                bot.simulationSession = this::clone;
                bot.cardsInSession = deck.getSessionCards();
            }
        }
    }

    private int currentCycle ;

    public void addNextPlayerCallback(BiConsumer<Integer, Integer> callback) {
        if (callback == null)
            return;

        onNextPlayer.add(callback);
    }
    public void addNextTrickCallback(Consumer<Integer> callback) {
        if (callback == null)
            return;

        onTrickComplete.add(callback);
    }

    private BiConsumer<Integer, Card> onBotPlayCard = null;

    public void setBotVisualHook(BiConsumer<Integer, Card> callback){
        onBotPlayCard = callback;
    }

    private void executeNextPlayerCallback(int prev, int curr) {
        for (var runnable : onNextPlayer)
            runnable.accept(prev, curr);
    }

    private void executeNextTrickCallback(int winner) {
        for (var runnable : onTrickComplete)
            runnable.accept(winner);
    }

    public void startRound() {
        deck.reshuffle();
        playedCards.clear();
        currentCycle = 1;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.length; i++) {
                players[(i + startPlayer) % players.length].addHand(deck.pop_());
            }
        }
    }

    private int count = 0;
    public void playTurn(Card card) {
        players[currentPlayer].removeHand(card);
        Table.add(card);
        deck.remove(card); // Failsafe
        var tmp = new HashSet<Card>(playedCards);
        playedCards.add(card);
        ++count;
        if(count != playedCards.size())
            throw new RuntimeException("FUCK we desynced");

        if(card == null)
            System.out.println("Damn");

        if(canGrabCard())
            players[currentPlayer].addHand(deck.pop());

        var previousPlayer = currentPlayer;
        currentPlayer = (currentPlayer + 1) % players.length;

        if (currentPlayer == startPlayer) {
            currentPlayer = startPlayer = determineCycleWinner();
            ++currentCycle;
            while (!Table.isEmpty()) {
                players[startPlayer].addCollectedCard(Table.remove(0));
            }

            executeNextTrickCallback(currentPlayer);

            // Why?
            if (gameOver()) {
                int playerWinner = 0;
                for (int i = 1; i < players.length; i++) {
                    if (players[i].Score() > players[playerWinner].Score())
                        playerWinner = i;
                }
            }
        }

        executeNextPlayerCallback(previousPlayer, currentPlayer);
    }
    public boolean canGrabCard(){
        int inHand = 0;
        for(var player : players){
            inHand += player.getHand().size();
        }
        return currentCycle < totalCycles-2;
    }

    private int determineCycleWinner() {
        int winner = 0;

        for (int i = 0; i < Table.size(); ++i)
            if (Table.get(i).compareTo(Table.get(winner), deck.getBriscola().suit, Table.get(0).suit) >= 1)
                winner = i;

        return (winner + startPlayer) % players.length;
    }

    public boolean gameOver() {
        return playedCards.equals(deck.getSessionCards());
    }

    public int getWinnerChickenDinner() {
        int currentMaxScore = 0;
        int maxScorePlayerIndex = 0;
        if(players[0].Score()==players[1].Score()){
            return -1;
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i].Score() > currentMaxScore) {
                currentMaxScore = players[i].Score();
                maxScorePlayerIndex = i;
            }
        }
        return maxScorePlayerIndex;
    }

    public int getScoreForTeam(int teamNumber) {
        if (!isTeamGame)
            return 0;

        int total = 0;

        for (int i = teamNumber; i < players.length; i += 2)
            total += players[i].Score();

        return total;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber % players.length];
    }

    public int getTeamNumber(int playerNumber) {
        return playerNumber % 2;
    }

    public boolean isBot(int playerNumber) {
        return (players[playerNumber] instanceof Bot);
    }

    public boolean isCurrentPlayerABot() {
        return players[currentPlayer] instanceof Bot;
    }

    public void botPlayTurn() {
        if (!(players[currentPlayer] instanceof Bot playerBot))
            return;

        try {
            playerBot.trickNumber = currentCycle;
            playerBot.playedCards = (HashSet<Card>)playedCards.clone();
            Card uhm = playerBot.MakeDecision((ArrayList<Card>)Table.clone(), (deck.getBriscola().suit));

            if(onBotPlayCard != null) {
                onBotPlayCard.accept(currentPlayer, uhm);
            }

            playTurn(uhm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulate() {
        if (Arrays.stream(players).anyMatch(p -> !(p instanceof Bot)))
            throw new UnsupportedOperationException("All players must be bots");

        while (!gameOver()) {
            botPlayTurn();
        }
    }

    public void simulate(int depth) {
        if (Arrays.stream(players).anyMatch(p -> !(p instanceof Bot)))
            throw new UnsupportedOperationException("All players must be bots");

        int i = 0;
        while (!gameOver() && (i++ < depth)) {
            botPlayTurn();
        }
    }

    @Override
    public GameSession clone() {
        var clonePlayers = new Player[players.length];
        for (int i = 0; i < players.length; ++i) {
            clonePlayers[i] = players[i].clone();
        }

        GameSession clone = new GameSession(clonePlayers);

        clone.deck = deck.clone();
        clone.currentPlayer = currentPlayer;
        clone.startPlayer = startPlayer;
        clone.playedCards.addAll(playedCards);
        clone.Table.addAll(Table);
        clone.count = count;
        clone.totalCycles = totalCycles;
        clone.currentCycle = currentCycle;
        return clone;
    }

    public Set<Card> getPlayedCards() {
        return Collections.unmodifiableSet(playedCards);
    }

}
