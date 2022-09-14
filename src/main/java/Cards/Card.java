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
        Two,
        Four,
        Five,
        Six,
        Seven,
        Jack,
        Queen,
        King,
        Ace,
        Three
    }

    public final Suit suit;
    public final Number number;

    // The score value of this card
    public final int value;

    private final int hashCode;

    public Card(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
        this.value = number.ordinal() < 6 ? 0 : number.ordinal();

        hashCode = Objects.hash(suit, number);
    }

    // Performs a comparison, will return 0 if both cards don't share the same suit
    @Override
    public int compareTo(Card other) {
        // Comparisons are only valid if they are the same suit
        if (suit != other.suit)
            return 0;

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

    // Allows the use of System.out.println(CARD) or similar string functions
    @Override
    public String toString() {
        return String.format("%s of %s (%d pts)", number.toString(), suit.toString(), value);
    }
}
