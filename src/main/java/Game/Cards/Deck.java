package Game.Cards;

import Game.Utils.SeededRandom;

import java.util.*;

public class Deck implements Cloneable {

    private SeededRandom rng ;
    private Random rng2 = new Random();
    private ArrayList<Card> cards = new ArrayList<>();

    private Card briscola = null;

    private int deckSize;

    private HashSet<Card> cardsInSession = new HashSet<>();

    public Deck() {
        this(40);
    }

    public Deck(int deckSize) {
        this.deckSize = deckSize;
        rng = new SeededRandom();
        reshuffle();
    }

    public Deck(int deckSize, int seed){
        this.deckSize = deckSize;
        rng = new SeededRandom(seed);
        reshuffle();
    }

    private Deck(boolean init) {

    }

    public void reshuffle() {
        // Create all the cards
        var cardsOrig = new ArrayList<Card>();
        cards = new ArrayList<Card>();
        cardsInSession.clear();
        for (Card.Suit suit : Card.Suit.values())
            for (Card.Number number : Card.Number.values())
                cardsOrig.add(new Card(suit, number));

        for (int i = 0; i < 40 - deckSize; ++i) {
            int a = i;
            cardsOrig.removeIf(c -> c.suit == Card.Suit.values()[a] && c.number == Card.Number.Two);
        }

        for (int i = cardsOrig.size()-1; i >= 0; --i) {
            cards.add(cardsOrig.remove(rng.nextInt(0, cardsOrig.size())));
        }

        briscola = cards.get(cards.size() - 1);
        cardsInSession.addAll(cards);
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
        return cards.remove(rng2.nextInt(0, Math.max(1, cards.size() - 1)));
    }
    public Card pop_() {
        return cards.remove(rng.nextInt(0, Math.max(1, cards.size() - 1)));
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
        clone.rng = new SeededRandom(rng.getState());
        clone.briscola = briscola;
        clone.cardsInSession = new HashSet<>(cardsInSession);
        clone.cards = new ArrayList<>(cards);

        clone.deckSize = deckSize;
        return clone;
    }
}
