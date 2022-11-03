package Game.Bots;

import Cards.Card;
import Cards.Card.Suit;
import Game.Player;

import java.util.List;
import java.util.Set;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    public Set<Card> playedCards;

    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(List<Card> cardsOnTable, Suit Briscola);
}
