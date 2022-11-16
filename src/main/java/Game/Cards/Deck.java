package Game.Cards;

import Game.Utils.SeededRandom;

import java.util.*;

public class Deck implements Cloneable {

    private static final Random rng = new Random();
    private ArrayList<Card> cards = new ArrayList<>();

    private Card briscola = null;

    private int deckSize;

    private HashSet<Card> cardsInSession = new HashSet<>();

    public Deck() {
        this(40);
    }

    public Deck(int deckSize) {
        this.deckSize = deckSize;
    }

    private Deck(boolean init) {

    }

    public void reshuffle() {
        // Create all the cards
        cards = new ArrayList<Card>();
        for (Card.Suit suit : Card.Suit.values())
            for (Card.Number number : Card.Number.values())
                cards.add(new Card(suit, number));

        for (int i = 0; i < 40 - deckSize; ++i) {
            int a = i;
            cards.removeIf(c -> c.suit == Card.Suit.values()[a]);
        }

        briscola = cards.get(cards.size() - 1);
    }

    public int size() {
        return cards.size();
    }

    public void add(int i, Card card) {
        cards.add(i, card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public Card pop() {
        return cards.get(rng.nextInt(0, Math.max(1, cards.size() - 1)));
    }

    public Card getBriscola() {
        return briscola;
    }

    public Set<Card> getSessionCards() {
        return Collections.unmodifiableSet(cardsInSession);
    }

    @Override
    public Deck clone() {
        Deck clone = new Deck(false);
        clone.briscola = briscola;
        clone.cardsInSession = new HashSet<>(cardsInSession);

        clone.deckSize = deckSize;
        return clone;
    }
}
