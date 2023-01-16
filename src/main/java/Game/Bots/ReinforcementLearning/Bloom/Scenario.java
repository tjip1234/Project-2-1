package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;
import Game.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Scenario implements Serializable {

    public final int trickNumber;
    public final Tuple<Integer, Integer> dominantCardOnTable;
    public final Integer briscolaSuit;
    public final Set<Tuple<Integer, Integer>> cardsInHand;

    public Scenario(Set<Card> cardsInHand, Card dominantCard, Card.Suit briscolaSuit, int trickNumber){
        this.cardsInHand = new HashSet<>();

        for(var item :cardsInHand)
            this.cardsInHand.add(new Tuple<>(item.suit.ordinal(), item.number.ordinal()));

        if(dominantCard != null) {
            this.dominantCardOnTable = new Tuple<>(dominantCard.suit.ordinal(), dominantCard.number.ordinal());
        }
        else{
            this.dominantCardOnTable = null;
        }

        this.briscolaSuit = briscolaSuit.ordinal();
        this.trickNumber = trickNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if(!(obj instanceof Scenario otherScenario))
            return false;


        return Objects.equals(dominantCardOnTable,otherScenario.dominantCardOnTable)
                && Objects.equals(briscolaSuit, otherScenario.briscolaSuit)
                && cardsInHand.equals(otherScenario.cardsInHand);
    }

    @Override
    public int hashCode() {
        return Objects.hash( dominantCardOnTable, briscolaSuit, cardsInHand);
    }
}
