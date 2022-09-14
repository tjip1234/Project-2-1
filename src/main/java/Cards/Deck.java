package Cards;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Deck extends Stack<Card> {
    private static final Random random = new Random();

    private Card briscola = null;

    public Deck() {
        reshuffle();
    }

    public void reshuffle() {
        this.clear();

        // Create all the cards
        var cards = new ArrayList<Card>();
        for (Card.Suit suit : Card.Suit.values())
            for (Card.Number number : Card.Number.values())
                cards.add(new Card(suit, number));

        // Put them in the stack
        while (!cards.isEmpty())
            push(cards.remove(random.nextInt(cards.size())));

        for (int i = 0; i < size(); i++)
            System.out.println(get(i));

        // Find the briscola ahead of time
        add(briscola = pop());
    }

    public Card getBriscola() {
        return briscola;
    }
}
