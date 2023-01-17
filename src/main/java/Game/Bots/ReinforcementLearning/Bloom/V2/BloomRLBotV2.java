package Game.Bots.ReinforcementLearning.Bloom.V2;

import Game.Bots.Bot;
import Game.Cards.Card;
import Game.Tuple;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BloomRLBotV2 extends Bot {
    private Tuple<State, CardTuple> lastMove = null;

    public static AtomicInteger winsThisSession = new AtomicInteger();

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        var dominant = FindDominantCard(cardsOnTable, Briscola);

        var state = State.From(getHand(), dominant, Briscola);

        var bestMove = getBestMoveFor(state);
        lastMove = new Tuple<>(state, bestMove);

        return bestMove.toCard();
    }

    private static final float alpha = 0.5f;
    private static final float gamma = 0.5f;
    public static  float epsilon = 0.1f;

    private static Map<State, Map<CardTuple, Float>> table = new ConcurrentHashMap<>();

    private final static Random rng = new Random();

    private CardTuple getBestMoveFor(State state){
        if(!table.containsKey(state) || rng.nextFloat() < epsilon){
            // A more convoluted way to randomly choose from a set, since it has no index
            var chosen = rng.nextInt(state.cardsInHand().size());
            var i = 0;
            for(var card : state.cardsInHand()){
                if(i++ == chosen)
                    return card;
            }
        }

        CardTuple bestCard = null;
        float bestScore = Float.NEGATIVE_INFINITY;

        var moves = table.get(state);

        for(var card : state.cardsInHand()){
            if(moves.get(card) > bestScore) {
                bestScore = moves.get(card);
                bestCard = card;
            }
        }

        return bestCard;
    }


    public void updateLastMove(float reward, List<Card> cardsOnTable, Card.Suit Briscola){
        if(lastMove == null)
            return;

        // Initialize state in table if needed
        if(!table.containsKey(lastMove.x())){
            var nested = new ConcurrentHashMap<CardTuple, Float>();
            for(var card : lastMove.x().cardsInHand())
                nested.put(card, 0f);

            table.put(lastMove.x(), nested);
        }

        float qScoreCurrent = table.get(lastMove.x()).get(lastMove.y());

        var dominant = FindDominantCard(cardsOnTable, Briscola);
        var state = State.From(getHand(), dominant, Briscola);

        float bestScorePossibleNextTrick = Float.NEGATIVE_INFINITY;
        if(table.containsKey(state)) {
            var nextMoves = table.get(state);
            for (var card : state.cardsInHand())
                bestScorePossibleNextTrick = Math.max(bestScorePossibleNextTrick, nextMoves.get(card));
        }
        else bestScorePossibleNextTrick = 0;

        float qScoreNew = qScoreCurrent + alpha * (reward + (gamma * bestScorePossibleNextTrick) - qScoreCurrent);

        table.get(lastMove.x()).put(lastMove.y(), qScoreNew);

        lastMove = null;
    }

    public static void writeTo(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(table);
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
            table = (Map<State, Map<CardTuple, Float>>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
