package Game;

import Cards.Card;
import com.example.project_2.HelloApplication;
import java.util.ArrayList;

public class Player {
    private ArrayList<Card> Hand;
    private ArrayList<Card> CollectedCards;

    public Player() {
        Hand = new ArrayList<>();
        CollectedCards = new ArrayList<>();
    }

    public int Score() {
        int score = 0;
        for (int i = 0; i < CollectedCards.size(); i++)
            score += CollectedCards.get(i).number.scoreValue;

        return score;
    }

    public void addHand(Card card, int whichPlayer) {
        Hand.add(card);
        try {
            HelloApplication.gameUI.drawAnimation(whichPlayer);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public ArrayList<Card> getHand() {
        return (ArrayList<Card>) Hand.clone();
    }

    public void removeHand(Card card) {
        Hand.remove(card);
    }

    public void addCollectedCard(Card card) {
        CollectedCards.add(card);
    }
}
