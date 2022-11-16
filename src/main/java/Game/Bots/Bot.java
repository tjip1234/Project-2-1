package Game.Bots;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import Game.GameSession;
import Game.Player;
import Game.Cards.Card;
import Game.Cards.Card.Suit;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    public Set<Card> playedCards;

    public Supplier<GameSession> simulationSession;

    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(List<Card> cardsOnTable, Suit Briscola) throws IOException;

    public final static Card FindDominantCard(List<Card> cardsOnTable, Suit Briscola) {
        return cardsOnTable.stream().max((lhs, rhs) -> lhs.compareTo(rhs, Briscola)).get();
    }
}
