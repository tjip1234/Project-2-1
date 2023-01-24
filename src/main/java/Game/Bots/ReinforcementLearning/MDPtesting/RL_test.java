package Game.Bots.ReinforcementLearning.MDPtesting;

import Game.Bots.Bot;
import Game.Bots.ReinforcementLearning.Gamestate_modified;
import Game.Bots.ReinforcementLearning.Tuple;
import Game.Bots.ReinforcementLearning.VirtualSave;
import Game.Cards.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class RL_test extends Bot {
    // State space and action space

    public static ArrayList<Tuple<Gamestate,double[]>> stateValues;
    public static Map<Gamestate, double[]> hashvalues;
    static final public String filepath = "src/main/java/Game/Bots/ReinforcementLearning/MDPtesting/savedParamModded.dat";
    public ArrayList<Tuple<Gamestate, Integer>> rewards;
    public double alpha = 0.5; // Learning rate
    private final double gamma = 0.9; // Discount factor
    public double epsilon = 0.1; // Exploration rate
    Gamestate state;

    public RL_test(double alpha, Gamestate k) {
        state = k;
        this.alpha = alpha;
        if (VirtualSave2.hashvalues == null)
            VirtualSave2.hashvalues = (Map<Gamestate, double[]>) readObjectFromFile(filepath);
        hashvalues = VirtualSave2.hashvalues;
        rewards = new ArrayList<>();
    }

    public void updateStateValues(Gamestate state, int action, double reward) {

        double[] thing = hashvalues.get(state);
        double maxFutureValue = Double.NEGATIVE_INFINITY;

        if (thing == null) {
            int index = -1;
            for (int i = 0; i < state.getHand().length; i++) {
                if (state.getHand()[i] == action) {
                    index = i;
                    break;
                }
            }
            double[] values = new double[3];
            values[index] = reward;
            hashvalues.put(state, values);
        }
        else {
            for (int i = 0; i < state.getHand().length; i++) {
                if (thing != null) {
                    maxFutureValue = Math.max(maxFutureValue, thing[i]);
                }
            }
            int index = getIndex(state.getHand(), action);
            double currentValue = thing[index];
            double addedstuff = (alpha * (reward + (gamma * maxFutureValue) - currentValue));
            double newValue = currentValue + addedstuff;
            double[] modified = new double[state.getHand().length];
            for(int i = 0 ; i < modified.length; ++i)
                if(i == index)
                    modified[i] = newValue;
                else
                    modified[i] = thing[i];

            hashvalues.put(state, modified);
        }
    }

    public Card chooseAction(Gamestate state) {
        // Try out a different move sometimes, to expand our horizons
        if (Math.random() < epsilon)
            return getHand().get((int) (Math.random() * getHand().size()));
        //afterwards to save time 1/10 of the time
        double[] thing = hashvalues.get(state);
        // We never encountered this scenario before
        if (thing == null)
            return getHand().get((int) (Math.random() * getHand().size()));

        // Exploit
        int bestAction = 0;
        double bestValue = -99999.99;
        for (int i = 0; i < state.getHand().length; i++) {
            if (thing[i] > bestValue) {
                bestAction = state.getHand()[i];
                bestValue = thing[i];
            }
        }
        return numbertocard(bestAction);

    }

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {

        //Card[] playedcards = getPlayedCards().toArray(new Card[0]);
        Card[] playedcards = cardsOnTable.toArray(new Card[0]);
        Card[] hand = getHand().toArray(new Card[0]);
        Card[] deck = playedCards.toArray(new Card[0]);
        ArrayList<Integer> h = new ArrayList<>();
        ArrayList<Integer> k = new ArrayList<>();
        ArrayList<Integer> l = new ArrayList<>();
        for (Card card : playedcards) {h.add(cardtonumber(card));}
        for (Card card : hand) {k.add(cardtonumber(card));}
        for (Card card : deck) {l.add(cardtonumber(card));}
        h.sort(Integer::compareTo);
        k.sort(Integer::compareTo);
        l.sort(Integer::compareTo);
        state.init(h.stream().mapToInt(i -> i).toArray(),k.stream().mapToInt(i -> i).toArray(),l.stream().mapToInt(i -> i).toArray(),Briscola.ordinal());
        Card action = chooseAction(state);

        //save states and execute rewards later
        Gamestate nextState;
        nextState = state.clone();
        rewards.add(new Tuple<>(nextState,cardtonumber(action)));


        return action;
    }
    public static void main(String[] args) {

        //ONLY USE TO RESET PARAMETERS MIGHT WASTE HOURS OF TRAINING!!!!!!!
        //hashvalues = new ConcurrentHashMap<>();
        //writeObjectToFile(hashvalues,filepath);
        hashvalues = (Map<Gamestate, double[]>) readObjectFromFile(filepath);
        System.out.print(hashvalues);
    }
    public void executeRewards(double reward){
        for (Tuple<Gamestate, Integer> rew:rewards) {
            updateStateValues(rew.x(), rew.y(), reward);
        }
        VirtualSave2.hashvalues = hashvalues;
    }
    public void save(){
        writeObjectToFile(VirtualSave.hashvalues, filepath);
    }
    public void scoreRewards(double reward){
        updateStateValues(rewards.get(rewards.size()-1).x(), rewards.get(rewards.size()-1).y(), reward);
        VirtualSave2.hashvalues = hashvalues;
    }
    public static int cardtonumber(Card card){
        return card.number.ordinal()+(card.suit.ordinal()*100);
    }
    public static Card numbertocard(int number){
        Card.Suit[] k = Card.Suit.values();
        Card.Number[] v = Card.Number.values();
        int ordinalsuit = (int)number/100;
        return new Card(k[ordinalsuit],v[number-ordinalsuit*100]);
    }
    public static void writeObjectToFile(Object obj, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object readObjectFromFile(String filePath) {
        Object obj = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static Tuple<Gamestate, double[]> getTuple(Gamestate_modified key) {
        for (Tuple<Gamestate, double[]> tuple : stateValues) {
            if (tuple.x().equals(key)) {
                // the tuple with the matching int array is found
                return tuple;
            }
        }
        return null;
    }
    private Tuple<Gamestate, double[]> getTuple_weird(Gamestate_modified state) {
        Stream<Tuple<Gamestate, double[]>> tupleStream = stateValues.stream()
                .filter(tuple -> tuple.x().equals(state));
        Optional<Tuple<Gamestate, double[]>> tuple = tupleStream.findFirst();
        Tuple<Gamestate, double[]> thing = tuple.orElse(null);
        return thing;
    }
    public static int getIndex(int[] k, int l) {
        for (int i = 0; i < k.length; i++) {
            if (k[i] == l)
                return i;
        }
        throw new RuntimeException("not part of the thing");
    }
}