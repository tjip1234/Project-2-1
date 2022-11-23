package Game.Bots.MCST;

import Game.Bots.Bot;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MCTS3_bot extends Bot {

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        return findCardToPlay(simulationSession.get(), 1000);
    }

    /**
     * Initialize Tree
     * @param board, an object representing the board
     * @return The tree
     */
    private Tree initializeTree(GameSession board){
        return new Tree(board, board.currentPlayer);
    }

    /**
     * Main method of the bot
     * @param board, an object representing the board
     * @param iterationCount an integer representing the count for how many iterations mcts runs
     * @return the best card to play
     */
    private Card findCardToPlay(GameSession board, int iterationCount) {
        Tree tree = initializeTree(board);
        
        while(iterationCount!=0){
            Node currentNode = tree.getRootNode();
            currentNode = selectionPhase(currentNode);
            backpropagationPhase(currentNode,simulationPhase(currentNode));
            iterationCount--;
        }
        return getWinningNode(tree.getRootNode()).getState().getCardPlayed();
    }

    /**
     * Selection phase of the four phases of mcts
     * @param rootNode the rootnode of the tree
     * @return the node from which we are going to do the next phase
     */
    private Node selectionPhase(Node rootNode){
        // depth =2 all children
        // depth >2 some heuristic

        Node currentNode = rootNode;

        int count = 0;
        //Goes through fully explored layers of the tree
        while (currentNode.getState().getPossibleStates().size() == 0) {
            Node tempNode = UTC.findPossibleNode(currentNode);
            if(tempNode == null){break;}
            currentNode = tempNode;

        }
        
        int exploredChildSize = currentNode.getListOfChildren().size();

        //Basic version
        return extensionPhase(currentNode);
    }

    /**
     * Extension phase of the four phases of mcts
     * @param targetParentNode the Targeted node of the tree from which a child is selected
     * @return the child of targeted node
     */
    private Node extensionPhase(Node targetParentNode){
        // Adds one of the child of targetNode
        State state = targetParentNode.getState().getRandomChildState();
        targetParentNode.getState().getPossibleStates().remove(state);
        Node targetNode = new Node(state);
        targetNode.setParentNode(targetParentNode);
        targetParentNode.getListOfChildren().add(targetNode);
        
        return targetNode;
    }

    /**
     * Simulation phase of the four phases of mcts
     * @param targetNode the node from which we simulate the game to the end
     * @return the score based on win, draw or lose 
     */
    private double simulationPhase(Node targetNode){
        //simulate one random game
        //simulate couple and return average
        //return normalized score in game instead of 1,0,-1
        State simulationState = targetNode.getState();
        double score;
        
        while(true){
            if(simulationState.getBoardState().gameOver()){
                if(simulationState.getBoardState().getWinnerChickenDinner() == simulationState.getRootPlayerNumber()){
                    State finalStateToExplore = simulationState;
                    //true if two players have the same score
                    if (Arrays.stream(simulationState.getBoardState().players).filter(
                                    c -> c.Score() == finalStateToExplore.getBoardState().players[finalStateToExplore.getRootPlayerNumber()].Score())
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
            simulationState = simulationState.getRandomChildState();
        }
        return score;
    }

    /**
     * Backpropagation phase of the four phases of mcts
     * @param targetNode the node from which we backpropagate to the root 
     */
    private void backpropagationPhase(Node targetNode, double score){
        targetNode.getState().addToVisitCount();

        if(targetNode.getParentNode()==null){
            return;
        }
        targetNode.getParentNode().getState().addWinScore(score);
        backpropagationPhase(targetNode.getParentNode(), score);
    }

    /**
     * Gets the childNode with the highest score
     * @param rootNode the first in the tree
     * @return the Node with the highest node
     */
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
