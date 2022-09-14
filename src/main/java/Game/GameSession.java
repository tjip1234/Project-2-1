package Game;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;
import java.util.Stack;

public class GameSession {
    public ArrayList<Player> players;
    public ArrayList<Card<String,String,Integer>> Table = new ArrayList<>();
    public int currentPlayer; //TODO whatever public stuff
    int startPlayer = 0;
    public String TrumpSuit;
    Stack<Card<String,String,Integer>> deck;
    public GameSession(int AmountOfPlayers){
        players = new ArrayList<Player>();
        currentPlayer = 0;

        for (int i = 0; i < AmountOfPlayers; i++) {
            players.add(new Player());
        }
    }

    public void startRound(){
        deck = getDeck();
        determineBriscola();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.size(); i++) {
                players.get((i+startPlayer)%players.size()).addHand(deck.pop());
            }
        }
    }
    public void playTurn(Card<String,String,Integer> card){
        players.get(currentPlayer).removeHand(card);
        Table.add(card);
        currentPlayer = (currentPlayer+1)%players.size();
        if (currentPlayer == startPlayer){
            startPlayer = determineCycleWinner();
            while(!Table.isEmpty()){
                players.get(startPlayer).addCollectedCard(Table.remove(0));
            }
            if (!deck.isEmpty()) {
                for (int i = 0; i < players.size(); i++) {
                    players.get((i + startPlayer) % players.size()).addHand(deck.pop());
                }
            }
            if (gameOver()){
                int playerWinner = 0;
                for (int i = 1; i < players.size(); i++) {
                    if (players.get(i).Score() > players.get(playerWinner).Score());
                        playerWinner = i; //TODO if the score is equal
                }
            }
        }
    }
    private int determineCycleWinner(){
        int winner = 0;
        for (int i = 1; i < Table.size(); i++) {
            if (Table.get(i).y().equals(Table.get(winner).y()) && Table.get(i).z() > Table.get(winner).z()) {
                winner = i;
            }
            else if(Table.get(i).y().equals(TrumpSuit)){
                winner = i;
            }
        }
        return (winner + startPlayer)%players.size();
    }
    private void determineBriscola(){
        Card<String,String,Integer> trumpCard = deck.pop();
        deck.add(trumpCard);
        TrumpSuit = trumpCard.y();
    }
    private boolean gameOver() {
        return players.get(0).getHand().isEmpty();
    }
    private Stack<Card<String,String,Integer>> getDeck(){
        Deck d = new Deck();
        return d.getDeck();
    }
}
