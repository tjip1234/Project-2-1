package Game.Bots.ReinforcementLearning.Bloom.V2;

import Game.Cards.Card;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record State (
        int briscolaOrdinal,
        CardTuple dominantCard,
        Set<CardTuple> cardsInHand) implements Serializable {

    public static State From(List<Card> cardsInHand, Card dominant, Card.Suit briscola){
        HashSet<CardTuple> cardsInHandSet = new HashSet<>();

        for(Card card : cardsInHand) {
            if(card == null)
                continue;
            cardsInHandSet.add(CardTuple.fromCard(card));
        }

        return new State(briscola.ordinal(), CardTuple.fromCard(dominant), cardsInHandSet);
    }
}
