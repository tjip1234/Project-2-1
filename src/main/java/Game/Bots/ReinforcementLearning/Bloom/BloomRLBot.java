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
    public void rewardAllMoves(double amount){
        if(amount > 0) {
            winsThisSession.incrementAndGet();
            scenarioManager.wins.incrementAndGet();
        }
        for (var d : encounteredScenarios)
            scenarioManager.updateScenario(d.scenario, d.choice, amount);

        encounteredScenarios.clear();
    }

    private Scenario createScenario(List<Card> cardsOnTable, Card.Suit Briscola){
        var dominant = FindDominantCard(cardsOnTable, Briscola);

        return new Scenario(new HashSet<>(getHand()), dominant, Briscola);
    }

    public static void writeTo(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(OutputStuff.createFromScenarioManager(scenarioManager));
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
            var outputStuff = (OutputStuff) ois.readObject();
            outputStuff.applyToScenarioManager(scenarioManager);
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
