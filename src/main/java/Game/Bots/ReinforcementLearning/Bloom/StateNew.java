package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;

import java.util.Set;

public record StateNew(Card.Suit briscolaOrdinal,
                       Card dominantCard,
                       int tableValue,
                       Set<Card> cardsInHand,
                       int remainingValuePotential){}