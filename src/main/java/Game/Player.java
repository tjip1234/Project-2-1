package Game;

import Cards.Card;

import java.util.ArrayList;

public class Player {
    ArrayList<Card<String,String,Integer>> Hand;
    ArrayList<Card<String,String,Integer>> CollectedCards;
    public Player(){
        Hand = new ArrayList<>();
        CollectedCards = new ArrayList<>();
    }
    public int Score() {
        int score = 0;
        for (int i = 0; i < CollectedCards.size(); i++) {
            if(CollectedCards.get(i).z() > 0)
                score += CollectedCards.get(i).z();
        }
        return score;
    }
    public void addHand(Card<String,String,Integer> card){
        Hand.add(card);
    }
    public ArrayList<Card<String,String,Integer>> getHand(){
        return Hand;
    }
    public void removeHand(Card<String,String,Integer> card){
        Hand.remove(card);
    }
    public void addCollectedCard(Card<String,String,Integer> card){
        CollectedCards.add(card);
    }
}
