package Game.Bots;

import Cards.Card;
import Game.Player;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(Card currentTopValue, Card Briscola);
}
