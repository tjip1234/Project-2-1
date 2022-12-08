package Game.Bots.ReinforcementLearning;

import Game.Bots.Bot;
import Game.Cards.Card;
import Game.GameSession;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class RL_bot extends Bot {
    // State space and action space

    public static ArrayList<Tuple<Gamestate,double[]>> stateValues;
    static final public String filepath = "src/main/java/Game/Bots/ReinforcementLearning/savedParam.dat";
    public ArrayList<Tuple<Gamestate, Integer>> rewards;
    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Discount factor
    private final double epsilon = 0.1; // Exploration rate

    public RL_bot() {
        stateValues = (ArrayList<Tuple<Gamestate, double[]>>) readObjectFromFile(filepath);
        rewards = new ArrayList<>();
    }

    public void updateStateValues(Gamestate state, int action, double reward) {
        // Create a stream of tuples that match the given Gamestate
        Stream<Tuple<Gamestate, double[]>> tupleStream = stateValues.stream()
                .filter(tuple -> tuple.x().equals(state));
        // Get the first tuple in the stream
        Optional<Tuple<Gamestate, double[]>> tuple = tupleStream.findFirst();
        Tuple<Gamestate, double[]> thing = tuple.orElse(null);

        if (thing == null) {
            int index = -1;
            for (int i = 0; i < state.hand.length; i++) {
                if (state.hand[i] == action) {
                    index = i;
                    break;
                }
            }
            double[] values = new double[3];
            values[index] = reward*10;
            stateValues.add(new Tuple<>(state, values));
        }
        else {
            int index = Arrays.binarySearch(state.hand, action);
            double currentValue = thing.y()[index];
            double addedstuff = (alpha / 1) * (reward - currentValue);
            double newValue = currentValue + (alpha / 1) * (reward - currentValue);
            double[] modified = Arrays.stream(state.hand)
                    .mapToDouble(value -> value - addedstuff/2)
                    .toArray();
            modified[index] = newValue;

            int indexoflist = stateValues.indexOf(thing);
            stateValues.set(indexoflist, new Tuple<>(state, modified));
        }
    }

    public Card chooseAction(Gamestate state) {
        if (Math.random() < epsilon) {
            // Explore: choose a random action
            return getHand().get((int) (Math.random() * getHand().size()));
        } else {
            // Exploit: choose the action with the highest expected value
            Stream<Tuple<Gamestate, double[]>> tupleStream = stateValues.stream()
                    .filter(tuple -> tuple.x().equals(state));
            // Get the first tuple in the stream
            Optional<Tuple<Gamestate, double[]>> tuple = tupleStream.findFirst();
            Tuple<Gamestate, double[]> thing = tuple.orElse(null);
            int bestAction = 0;
            double bestValue = -99999.99;
            if (thing != null) {
                for (int i = 0; i < thing.x().hand.length; i++) {
                    if (thing.y()[i] > bestValue) {
                        bestAction = thing.x().hand[i];
                        bestValue = thing.y()[i];
                    }
                }
            }
            else {
                // No information about this state: choose a random action
                return getHand().get((int) (Math.random() * getHand().size()));
            }
            return numbertocard(bestAction);

        }
    }

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {

        //Card[] playedcards = getPlayedCards().toArray(new Card[0]);
        Card[] playedcards = null;
        Card[] hand = getHand().toArray(new Card[0]);
        ArrayList<Integer> h = new ArrayList<>();
        ArrayList<Integer> k = new ArrayList<>();
        for (Card card : playedcards) {
            h.add(cardtonumber(card));
        }
        for (Card card : hand) {
            k.add(cardtonumber(card));
        }
        Gamestate state = new Gamestate(h.stream().mapToInt(i -> i).toArray(),k.stream().mapToInt(i -> i).toArray(),Briscola.ordinal());
        // Choose an action
        Card action = chooseAction(state);

        //save states and execute rewards later
        Gamestate nextState;
        nextState = state.clone();
        rewards.add(new Tuple<>(nextState,cardtonumber(action)));


        return action;
    }
    public static void main(String[] args) {

        //ONLY USE TO RESET PARAMETERS MIGHT WASTE HOURS OF TRAINING!!!!!!!
        //stateValues = new ArrayList<Tuple<Gamestate,double[]>>();
        //writeObjectToFile(stateValues,filepath);
        stateValues = (ArrayList<Tuple<Gamestate, double[]>>) readObjectFromFile(filepath);
        System.out.print(stateValues);
    }
    public void executeRewards(double reward){
        for (Tuple<Gamestate,Integer> rew:rewards) {
            updateStateValues(rew.x(), rew.y(), reward);
        }
        writeObjectToFile(stateValues,filepath);
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
}