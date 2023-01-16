package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;
import Game.Tuple;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ScenarioManager implements Serializable
{
    public AtomicInteger wins = new AtomicInteger();

    public Map<Scenario, Map<Tuple<Integer, Integer>, Double>> scenarios = new HashMap<>();

    private static Map<Scenario, Integer> mostEncounteredScenarios = new HashMap<>();

    private float alpha = 0.1f;

    public void addScenario(Scenario scenario){
        var map = new HashMap<Tuple<Integer, Integer>, Double>();
        for(var Card : scenario.cardsInHand)
            map.put(Card, 0.0);

        scenarios.put(scenario, map);
    }

    private Semaphore lock = new Semaphore(1, true);

    public void updateScenario(Scenario scenario, Card chosen, double amount) {
        try {
            lock.acquire();
            if (!scenarios.containsKey(scenario))
                addScenario(scenario);

            mostEncounteredScenarios.merge(scenario, 1, Integer::sum);

            var cards = scenarios.get(scenario);

            cards.merge(tupleFromCard(chosen), amount, Double::sum);
            lock.release();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    private static Random random = new Random();

    public Card getBestMoveFor(Scenario scenario){
        if(!scenarios.containsKey(scenario)) {
            return cardFromTuple((Tuple<Integer, Integer>)scenario.cardsInHand.toArray()[random.nextInt(scenario.cardsInHand.size())]);
        }

        Tuple<Integer, Integer> bestCard = null;

        double bestValue = -Double.MAX_VALUE;

        for(var card : scenario.cardsInHand)
        {
            double newVal = scenarios.get(scenario).get(card);
            if(newVal > bestValue){
                bestValue = newVal;
                bestCard = card;
            }
        }

        var excluded = new ArrayList<>(scenarios.get(scenario).keySet());

        if(excluded.size() > 1 && random.nextFloat() < 0.2f){
            excluded.remove(bestCard);
            return cardFromTuple(excluded.get(random.nextInt(excluded.size())));
        }

        return cardFromTuple(bestCard);
    }


    public void Trim(){
        for(var scen : mostEncounteredScenarios.keySet()){
            int count = mostEncounteredScenarios.get(scen);

            if(count == 1)
                scenarios.remove(scen);
        }
    }

    public static Card cardFromTuple(Tuple<Integer, Integer> card){
        return new Card(Card.suitVal[card.x()], Card.numberVals[card.y()]);
    }

    public static Tuple<Integer, Integer> tupleFromCard(Card card){
        return new Tuple<Integer, Integer>(card.suit.ordinal(), card.number.ordinal());
    }
}
