package Game.Bots.ReinforcementLearning;

import Game.Bots.Bot;
import Game.Cards.Card;
import Game.GameSession;

import java.io.IOException;
import java.util.*;

public class RL_bot extends Bot {
    // State space and action space
    private HashMap<GameSession, HashMap<Card, Double>> stateValues;
    private HashMap<GameSession, HashMap<Card, Integer>> stateCounts;
    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Discount factor
    private final double epsilon = 0.1; // Exploration rate

    public RL_bot() {
        stateValues = new HashMap<>();
        stateCounts = new HashMap<>();
    }

    public void updateStateValues(GameSession state, Card action, double reward) {
        var actions = stateValues.get(state);
        if (actions == null) {
            actions = new HashMap<>();
            stateValues.put(state, actions);
        }
        var count = stateCounts.get(state);
        if (count == null) {
            count = new HashMap<>();
            stateCounts.put(state, count);
        }
        double currentValue = actions.getOrDefault(action, 0.0);
        int n = count.getOrDefault(action, 0);
        double newValue = currentValue + (alpha / (n + 1)) * (reward - currentValue);
        actions.put(action, newValue);
        count.put(action, n + 1);
    }

    public Card chooseAction(GameSession state) {
        if (Math.random() < epsilon) {
            // Explore: choose a random action
            return getHand().get((int) (Math.random() * getHand().size()));
        } else {
            // Exploit: choose the action with the highest expected value
            Card bestAction = null;
            double bestValue = Double.NEGATIVE_INFINITY;
            var actions = stateValues.get(state);
            if (actions != null) {
                for (var entry : actions.entrySet()) {
                    if (entry.getValue() > bestValue && getHand().contains(entry.getKey())) {
                        bestAction = entry.getKey();
                        bestValue = entry.getValue();
                    }
                }
            }
            if (bestAction == null) {
                // No information about this state: choose a random action
                return getHand().get((int) (Math.random() * getHand().size()));
            } else {
                return bestAction;
            }
        }
    }

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola, GameSession game) throws IOException {

        // Choose an action
        var action = chooseAction(game);

        // Take the action and observe the reward
        game.playTurn(action);
        var reward = game.getPlayer(0).Score();

        // Update the state values
        var nextState = game.clone();
        updateStateValues(nextState, action, reward);

        return action;
    }




}