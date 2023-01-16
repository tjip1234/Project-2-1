package Game.Bots.ReinforcementLearning.Bloom;

import Game.Bots.Bot;
import Game.Cards.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BloomRLBot extends Bot {
    private record Decision(Scenario scenario, Card choice){}
    public static ScenarioManager scenarioManager = new ScenarioManager();
    private ArrayList<Decision> encounteredScenarios = new ArrayList<>();

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        var scen = createScenario(cardsOnTable, Briscola);

        var bestMove = scenarioManager.getBestMoveFor(scen);
        encounteredScenarios.add(new Decision(scen, bestMove));

        if(bestMove == null)
            return null;

        return bestMove;
    }

    public static AtomicInteger winsThisSession = new AtomicInteger();

    public void rewardLastMove(double amount){
        var d = encounteredScenarios.get(encounteredScenarios.size()-1);
        scenarioManager.updateScenario(d.scenario, d.choice, amount * ((21-trickNumber)/21f));
    }
    public void rewardAllMoves(double amount){
        var tri = trickNumber+1;
        for (var d : encounteredScenarios) {
            double originalValue = 0;
            double maxFutureValue = 0;

            if(scenarioManager.scenarios.containsKey(d.scenario)) {
                var scenarioMap = scenarioManager.scenarios.get(d.scenario);
                originalValue = scenarioMap.get(ScenarioManager.tupleFromCard(d.choice));

                maxFutureValue = scenarioManager.scenarios.get(d.scenario).values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            }

            var addition = (0.1 * (amount + ((tri/(trickNumber+1)) * maxFutureValue) - originalValue));
            scenarioManager.updateScenario(d.scenario, d.choice, addition);
            tri--;
        }
    }

    private Scenario createScenario(List<Card> cardsOnTable, Card.Suit Briscola){
        var dominant = FindDominantCard(cardsOnTable, Briscola);

        return new Scenario(new HashSet<>(getHand()), dominant, Briscola, trickNumber);
    }

    public static void writeTo(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scenarioManager);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readFromFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            scenarioManager = (ScenarioManager) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
