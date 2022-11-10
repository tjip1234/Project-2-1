package Game.Bots.MCST;

import Game.Bots.Bot;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class MCST_bot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) {
        return findCardToPlay(simulationSession.get(), 1000);
    }

    public Card findCardToPlay(GameSession board, int iterationCount) {
        Tree tree = new Tree(board, board.currentPlayer);
        Node rootNode = tree.getRootNode();
        List<State> possibleRootStates = rootNode.getState().getAllPossibleStates();
        for (State possibleState : possibleRootStates) {
            Node newNode = new Node(possibleState);
            newNode.setParentNode(rootNode);
            rootNode.getListOfChildren().add(newNode);
        }
        // Possible to do time-based instead of iteration based
        while (iterationCount > 0) {
            // Selection
            Node currentNode = rootNode;
            while (currentNode.getListOfChildren().size() != 0) {
                currentNode = UTC.findPossibleNode(currentNode);
            }

            // Extension
            if (!currentNode.getState().getBoard().gameOver()) {
                List<State> possibleStates = currentNode.getState().getAllPossibleStates();
                for (State possibleState : possibleStates) {
                    Node newNode = new Node(possibleState);
                    newNode.setParentNode(currentNode);
                    currentNode.getListOfChildren().add(newNode);
                }
            }

            // Simulation
            Stack<State> stateToExplore = new Stack<>();
            stateToExplore.push(currentNode.getState());
            int score = 0;
            // TODO draw
            while (true) {
                if (stateToExplore.peek().getBoard().gameOver()) {
                    if (stateToExplore.peek().getBoard().getWinnerChickenDinner() == board.currentPlayer) {
                        State finalStateToExplore = stateToExplore.peek();
                        if (Arrays.stream(stateToExplore.peek().getBoard().players).filter(
                                c -> c.Score() == finalStateToExplore.getBoard().players[board.currentPlayer].Score())
                                .count() > 1) {
                            score = 0;
                        } else {
                            score = 1;
                        }
                    } else {
                        score = -1;
                    }
                    break;
                }
                stateToExplore.push(stateToExplore.peek().getRandomChildState());
            }

            // Backpropagation
            while (!stateToExplore.empty()) {
                State tempState = stateToExplore.pop();
                tempState.addToVisitCount();
                tempState.addWinScore(score);
            }
            iterationCount--;
        }
        var winner = getWinningNode(rootNode).getState();
        return winner.cardPlayed;
    }

    private Node getWinningNode(Node rootNode) {
        Node highestValueNode = null;
        for (Node node : rootNode.getListOfChildren()) {
            if (highestValueNode == null) {
                highestValueNode = node;
            } else if (node.getState().getScoreForState() > highestValueNode.getState().getScoreForState()) {
                highestValueNode = node;
            }
        }
        return highestValueNode;
    }
}
