package Game.Cards;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Comparable<Card>, Serializable {
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

    // For serialization purposes only
    private Card(){
        suit = null;
        number = null;
    }

    public final Suit suit;
    public final Number number;

    /**
     * Constructor for a card
     *
     * @param suit   of the card
     * @param number of the card
     */
    public Card(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
    }

    /**
     * Performs a comparison, will return 0 if both cards don't share the same suit
     *
     * @param other card to be compared
     * @return which card won
     */
    @Override
    public int compareTo(Card other) {
        // A card is better than no card
        if (other == null)
            return 1;

        int scoreDifference = Integer.compare(number.scoreValue, other.number.scoreValue);

        if (scoreDifference != 0)
            return scoreDifference;

        // Fallback to traditional deck ordering if value is identical
        return Integer.compare(number.ordinal(), other.number.ordinal());
    }


    // Performs a comparison, but also take briscola suit priority into account
    public int compareTo(Card other, Suit briscolaSuit, Suit... dominantSuit) {
        if (other == null)
            return 1;

        if (suit == briscolaSuit) {
            if (other.suit == briscolaSuit)
                return compareTo(other);

            return 1;
        }

        if(dominantSuit.length > 0) {
            var dom = dominantSuit[0];
            if (suit == dom) {
                if (other.suit == dom)
                    return compareTo(other);

                if (other.suit == briscolaSuit)
                    return -1;

                return 1;
            }

            if (other.suit == dom)
                return -1;
        }

        if(other.suit == briscolaSuit)
            return -1;

        return compareTo(other);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card otherCard))
            return false;

        return (suit == otherCard.suit && number == otherCard.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, number);
    }

    @Override
    public String toString() {
        return String.format("%s of %s (%d pts)", number.toString(), suit.toString(), number.scoreValue);
    }
}
