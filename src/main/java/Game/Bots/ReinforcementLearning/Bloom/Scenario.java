package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Scenario implements Serializable {
    public final Card dominantCardOnTable;
    public final Card.Suit briscolaSuit;
    public final Set<Card> cardsInHand;

    public Scenario(Set<Card> cardsInHand, Card dominantCard, Card.Suit briscolaSuit){
        this.cardsInHand = cardsInHand;
        this.dominantCardOnTable = dominantCard;
        this.briscolaSuit = briscolaSuit;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(!(obj instanceof Scenario otherScenario))
            return false;


        return Objects.equals(dominantCardOnTable,otherScenario.dominantCardOnTable) && briscolaSuit == otherScenario.briscolaSuit && cardsInHand.equals(otherScenario.cardsInHand);
    }

    @Override
    public int hashCode() {
        return Objects.hash( dominantCardOnTable, briscolaSuit, cardsInHand);
    }
}
