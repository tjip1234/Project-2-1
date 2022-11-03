package Game.Bots;

import Game.Cards.Card;
import Game.Cards.Card.Suit;
import Game.GameSession;
import Game.Player;

import java.security.Provider;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    public Set<Card> playedCards;

    public Supplier<GameSession> simulationSession;

    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(List<Card> cardsOnTable, Suit Briscola);
}
