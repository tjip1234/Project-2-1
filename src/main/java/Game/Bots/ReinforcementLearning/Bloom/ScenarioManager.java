package Game.Bots.ReinforcementLearning.Bloom;

import Game.Cards.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ScenarioManager
{
    public AtomicInteger wins = new AtomicInteger();

    public Map<Scenario, Map<Card, Double>> scenarios = new HashMap<>();

    private static Map<Scenario, Integer> mostEncounteredScenarios = new HashMap<>();

    public void addScenario(Scenario scenario){
        var map = new HashMap<Card, Double>();
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

            cards.merge(chosen, amount, Double::sum);
            lock.release();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    private static Random random = new Random();

    public Card getBestMoveFor(Scenario scenario){

        if(!scenarios.containsKey(scenario) || random.nextFloat() < 0.2f){
            return (Card)scenario.cardsInHand.toArray()[random.nextInt(scenario.cardsInHand.size())];
        }

        Card bestCard = null;

        double bestValue = -Double.MAX_VALUE;

        for(var card : scenario.cardsInHand)
        {
            double newVal = scenarios.get(scenario).get(card);
            if(newVal > bestValue){
                bestValue = newVal;
                bestCard = card;
            }
        }
        if(bestCard == null)
            return null;

        return bestCard;
    }


    public void Trim(){
        for(var scen : mostEncounteredScenarios.keySet()){
            int count = mostEncounteredScenarios.get(scen);

            if(count == 1)
                scenarios.remove(scen);
        }
    }
}
