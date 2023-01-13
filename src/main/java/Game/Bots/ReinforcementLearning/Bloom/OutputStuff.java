package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class OutputStuff implements Serializable {
    public Card dominantCards[];
    public Card.Suit briscola[];
    public Card cardsInHand[][];
    public double weights[][];

    public static OutputStuff createFromScenarioManager(ScenarioManager manager){
        int size = manager.scenarios.size();

        var output = new OutputStuff();
        output.dominantCards = new Card[size];
        output.briscola = new Card.Suit[size];
        output.cardsInHand = new Card[size][];
        output.weights = new double[size][];
        int i = 0;
        for(var entry : manager.scenarios.entrySet()){
            output.dominantCards[i] = entry.getKey().dominantCardOnTable;
            output.briscola[i] = entry.getKey().briscolaSuit;

            output.cardsInHand[i] = new Card[entry.getValue().size()];
            output.weights[i] = new double[entry.getValue().size()];

            int j = 0;
            for(var entry2 : entry.getValue().entrySet()){
                output.cardsInHand[i][j] = entry2.getKey();
                output.weights[i][j] = entry2.getValue();
                ++j;
            }

            ++i;
        }

        return output;
    }

    public void applyToScenarioManager(ScenarioManager manager){
        for(int i= 0; i < dominantCards.length;++i){
            var cards = cardsInHand[i];
            var scen = new Scenario(new HashSet<Card>(List.of(cardsInHand[i])), dominantCards[i], briscola[i]);

            var valueMap = new HashMap<Card, Double>();

            for(int j = 0; j < cardsInHand[i].length; ++j){
                valueMap.put(cardsInHand[i][j], weights[i][j]);
            }

            manager.scenarios.put(scen, valueMap);
        }
    }



}
