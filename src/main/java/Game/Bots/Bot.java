package Game.Bots;

import Game.Cards.Card;
import Game.Cards.Card.Suit;
import Game.GameSession;
import Game.Player;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

// Used by gamesession to determine the choice.
// MakeDecision() must be implemented
// May contain fields for self use.
public abstract class Bot extends Player {
    public Set<Card> playedCards;
    public Set<Card> cardsInSession;

    public Supplier<GameSession> simulationSession;

    public int trickNumber = 0;

    /// Makes a choosing a card based on the current situation
    public abstract Card MakeDecision(List<Card> cardsOnTable, Suit Briscola) throws IOException;

    public static Card FindDominantCard(List<Card> cardsOnTable, Suit Briscola) {
        if (cardsOnTable.size() == 0)
            return null;

        return cardsOnTable.stream().max((lhs, rhs) -> lhs.compareTo(rhs, Briscola, cardsOnTable.get(0).suit)).get();
    }

    public static int getScoreForTable(Collection<Card> cardsOnTable){
        int sum = 0;
        for(Card card : cardsOnTable)
            sum += card.number.scoreValue;

        return sum;
    }
}
