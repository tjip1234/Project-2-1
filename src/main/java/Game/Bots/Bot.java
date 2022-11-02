package Game.Bots;

import java.util.ArrayList;
import java.util.HashSet;

import Cards.Card;
import Cards.Card.Suit;
import Game.Player;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    public HashSet<Card> playedCards;

    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(ArrayList<Card> cardsOnTable, Suit Briscola);
}
