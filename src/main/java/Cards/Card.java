package Cards;

import java.util.Objects;

public class Card implements Comparable<Card> {
    public static enum Suit {
        Hearts,
        Diamonds,
        Spades,
        Clubs
    }

    public static enum Number {
        Two(0),
        Three(10),
        Four(0),
        Five(0),
        Six(0),
        Seven(0),
        Jack(2),
        Queen(3),
        King(4),
        Ace(11);

        public final int scoreValue;


        Number(int scoreValue) {
            this.scoreValue = scoreValue;
        }
    }

    public final Suit suit;
    public final Number number;

    private final int hashCode;

    /**
     * Constructor for a card
     * @param suit of the card
     * @param number of the card
     */
    public Card(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;

        hashCode = Objects.hash(suit, number);
    }

    /**
     * Performs a comparison, will return 0 if both cards don't share the same suit
     * @param other card to be compared
     * @return which card won
     */
    @Override
    public int compareTo(Card other) {
        // Comparisons are only valid if they are the same suit
        if (suit != other.suit)
            return 0;

        int scoreDifference = Integer.compare(number.scoreValue, other.number.scoreValue);
        if (scoreDifference != 0)
            return scoreDifference;

        // Fallback to traditional deck ordering if value is identical
        return Integer.compare(number.ordinal(), other.number.ordinal());
    }

    // Performs a comparison, but also take briscola suit priority into account
    public int compareTo(Card other, Suit briscolaSuit) {
        var valueResult = compareTo(other);
        if (valueResult != 0)
            return valueResult;

        if (suit == briscolaSuit)
            return 1;

        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card))
            return false;

        Card otherCard = (Card) other;

        return (suit == otherCard.suit && number == otherCard.number);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
    @Override
    public String toString() {
        return String.format("%s of %s (%d pts)", number.toString(), suit.toString(), number.scoreValue);
    }
}
