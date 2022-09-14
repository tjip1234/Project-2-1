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

    public final int value;

    private final int hashCode;

    public Card(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
        this.value = number.ordinal() < 6 ? 0 : number.ordinal();

        hashCode = Objects.hash(suit, number);
    }

    @Override
    public int compareTo(Card other) {
        if (suit != other.suit)
            return 0;

        return Integer.compare(number.ordinal(), other.number.ordinal());
    }

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
        return String.format("%s of %s (%d pts)", number.toString(), suit.toString(), value);
    }

    public static void main(String[] args) {
        Card card1 = new Card(Suit.Hearts, Number.Four);
        Card card2 = new Card(Suit.Hearts, Number.Three);

        System.out.println(card1);
        System.out.println(card2);
        if (card1.compareTo(card2) < 0)
            System.out.printf("%s is worth more than %s", card2, card1);
    }
}
