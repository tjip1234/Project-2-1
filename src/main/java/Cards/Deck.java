package Cards;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Deck extends Stack<Card> {
    private static final Random random = new Random();

    private Card briscola = null;

    private int deckSize;

    public Deck() {
        this(40);
    }

    public Deck(int deckSize) {
        this.deckSize = deckSize;
        reshuffle();
    }

    public void reshuffle() {
        this.clear();

        // Create all the cards
        var cards = new ArrayList<Card>();
        for (Card.Suit suit : Card.Suit.values())
            for (Card.Number number : Card.Number.values())
                cards.add(new Card(suit, number));

        int cardsToRemove = 40 - deckSize;
        // Put them in the stack
        while (!cards.isEmpty()) {
            var card = cards.remove(random.nextInt(cards.size()));

            // We don't add this card.
            if (card.number == Card.Number.Two && cardsToRemove > 0) {
                cardsToRemove--;
                continue;
            }

            push(card);
        }

        // Find the briscola ahead of time, and add it to the bottom of the stack
        add(0, briscola = pop());
    }

    public Card getBriscola() {
        return briscola;
    }

}
