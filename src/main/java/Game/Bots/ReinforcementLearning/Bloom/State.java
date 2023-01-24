package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;

import java.util.Set;

// Using this makes training much, much faster. At the cost of a larger serialized file size
public record State(
        Card.Suit briscolaOrdinal,
        Card dominantCard,
        Set<Card> cardsInHand){
}
