package Game.Bots.ReinforcementLearning.Bloom;

import Game.Bots.Bot;
import Game.Bots.ReinforcementLearning.Tuple;
import Game.Cards.Card;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BloomRLBotV2 extends Bot {
    private Tuple<State, Card> lastMove = null;

    public static AtomicInteger winsThisSession = new AtomicInteger();

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        var dominant = FindDominantCard(cardsOnTable, Briscola);

        var state = new State(Briscola, dominant, new HashSet<>(getHand()));

        var bestMove = getBestMoveFor(state);
        lastMove = new Tuple<>(state, bestMove);

        return bestMove;
    }

    // For MCTS
    public static Card getWorstCard(List<Card> cardOnTable, Card.Suit briscola, List<Card> hand){
        var dominant = FindDominantCard(cardOnTable, briscola);

        var state = new State(briscola, dominant, new HashSet<>(hand));

        if(!table.containsKey(state))
            return null;

        Card worstCard = null;
        float worstScore = Float.POSITIVE_INFINITY;

        var moves = table.get(state);

        for (var card : state.cardsInHand()) {
            if (moves.get(card) < worstScore) {
                worstScore = moves.get(card);
                worstCard = card;
            }
        }

        return worstCard;
    }

    public static ArrayList<Tuple<Card, Float>> getCardScores(List<Card> cardOnTable, Card.Suit briscola, List<Card> hand){
        var dominant = FindDominantCard(cardOnTable, briscola);

        var state = new State(briscola, dominant, new HashSet<>(hand));

        if(!table.containsKey(state))
            return null;

        var result = new ArrayList<Tuple<Card,Float>>();
        var Choices = table.get(state);

        for(var card : hand)
            result.add(new Tuple<>(card, Choices.get(card)));

        return result;
    };

    private static final float alpha = 0.00001f;
    private static final float gamma = 0.9f;
    public float epsilon = 0.3f;

    public BloomRLBotV2(float epsilon){
        this.epsilon = epsilon;
    }

    private static ConcurrentHashMap<State, ConcurrentHashMap<Card, Float>> table = new ConcurrentHashMap<>();

    private final static Random rng = new Random();

    private Card getBestMoveFor(State state) {
        if (!table.containsKey(state)) {
            // A more convoluted way to randomly choose from a set, since it has no index
            var chosen = rng.nextInt(state.cardsInHand().size());
            var i = 0;
            for (var card : state.cardsInHand()) {
                if (i++ == chosen)
                    return card;
            }
        }

        Card bestCard = null;
        float bestScore = Float.NEGATIVE_INFINITY;

        var moves = table.get(state);

        for (var card : state.cardsInHand()) {
            if (moves.get(card) > bestScore) {
                bestScore = moves.get(card);
                bestCard = card;
            }
        }

        // Random deviation chance
        if(state.cardsInHand().size() > 1 && rng.nextFloat() < epsilon){
            var excluded = new ArrayList<Card>(state.cardsInHand());
            excluded.remove(bestCard);

            return excluded.get(rng.nextInt(excluded.size()));
        }
        return bestCard;
    }

    public void updateLastMove(float reward, List<Card> cardsOnTable, Card.Suit Briscola, Set<Card> playedCards) {
        if (lastMove == null)
            return;

        if(reward > 0) {
            assert lastMove.y().number != null;
            reward -= lastMove.y().number.scoreValue;
        }

        // Initialize state in table if needed
        if (!table.containsKey(lastMove.x())) {
            var nested = new ConcurrentHashMap<Card, Float>();
            for (var card : lastMove.x().cardsInHand())
                nested.put(card, 0f);

            table.put(lastMove.x(), nested);
        }

        float qScoreCurrent = table.get(lastMove.x()).get(lastMove.y());

        var dominant = FindDominantCard(cardsOnTable, Briscola);

        var tableValue = getScoreForTable(cardsOnTable);
        var remaining =  getScoreForTable(playedCards) + tableValue;

        var state = new State(Briscola, dominant,  new HashSet<>(getHand()));

        float bestScorePossibleNextTrick = Float.NEGATIVE_INFINITY;
        if (table.containsKey(state)) {
            var nextMoves = table.get(state);
            for (var card : state.cardsInHand())
                bestScorePossibleNextTrick = Math.max(bestScorePossibleNextTrick, nextMoves.get(card));
        } else
            bestScorePossibleNextTrick = remaining;

        float qScoreNew = qScoreCurrent + alpha * (reward + (gamma * bestScorePossibleNextTrick) - qScoreCurrent);

        table.get(lastMove.x()).put(lastMove.y(), qScoreNew);

        lastMove = null;
    }

    private static Kryo kryo;
    static {
        kryo = new Kryo();

        kryo.register(ConcurrentHashMap.class);
        kryo.register(State.class);
        kryo.register(Card.class);
        kryo.register(Card.Suit.class);
        kryo.register(Card.Number.class);
        kryo.register(HashSet.class);

        BloomRLBotV2.InitFromFile();
    }

    public static void writeTo(String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            var oos = new Output(fos);
            kryo.writeObject(oos, table);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitFromFile(){
        readFromFile("BloomStuffV2s");
    }

    public static void readFromFile(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            var ois = new Input(fis);
            table = kryo.readObject(ois, ConcurrentHashMap.class);
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
