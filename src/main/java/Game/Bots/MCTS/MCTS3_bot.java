package Game.Bots.MCTS;

import Game.Bots.Bot;
import Game.Bots.ReinforcementLearning.Bloom.BloomRLBotV2;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MCTS3_bot extends Bot {


    private double UTC_constant;
    private int iterationCount;

    /**
     * Constructor for MCTS
     * @param iterationCount how many iterations of the algorithm should run
     * @param UTC_constant UTC constant for UTC algorithm (Higher = more focus on less explored states)
     */
    public MCTS3_bot(int iterationCount, double UTC_constant){
        this.iterationCount = iterationCount;
        this.UTC_constant = UTC_constant;
    }

    /**
     * Method from interface which all bots share
     * @param cardsOnTable Table cards from current state
     * @param Briscola Briscola Suit
     * @return Card which bot decided on
     * @throws IOException
     */
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        return findCardToPlay(simulationSession.get(), iterationCount);
    }

    /**
     * Initialize Tree
     * @param board, an object representing the board
     * @return The tree
     */
    public Tree initializeTree(GameSession board, int recursionCounter) {

        Tree tree = new Tree(board, board.currentPlayer);
        tree.getRootNode().getState().createAllPossibleStates();
        int x = tree.getRootNode().getState().getPossibleUncheckedStates().size();

        for (int i = 0; i < x; i++) {
            Node node = extensionPhase(tree.getRootNode());
            backpropagationPhase(node, simulationPhase(node));
        }


        return tree;
    }

    /**
     * Main method of the bot
     * @param board, an object representing the board
     * @param iterationCount an integer representing the count for how many iterations mcts runs
     * @return the best card to play
     */
    private Card findCardToPlay(GameSession board, int iterationCount) throws IOException {

        Tree tree = initializeTree(board,1);
        int maxIteration = iterationCount;
        while(iterationCount!=0){
            Node currentNode = tree.getRootNode();
            currentNode = selectionPhase(currentNode, maxIteration-iterationCount);
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
    public Node selectionPhase(Node rootNode, int iterationCount){


        Node currentNode = rootNode;
        //While the node has is not terminal and has no children
        while(currentNode.getListOfChildren().size()>0){
            //10% chance to stop here at random (Done to increase the ammount of exploration)
            if(((currentNode.getState().getPossibleUncheckedStates().size()>0)&&(Math.random()<0.1))){
                break;
            }

            //if opponents turn, choose random explored state
            if(currentNode.getState().getBoardState().currentPlayer!=currentNode.getState().rootPlayerNumber){
                currentNode = currentNode.getListOfChildren().get((int)(Math.random()*currentNode.getListOfChildren().size()));
                continue;
            }

            //If it's our turn then use UTC to select the next node
            currentNode = UTC.findPossibleNode(currentNode,UTC_constant);
        }

        //FailSave for when the selected node is terminal and we can not extend it
        if(currentNode.getState().getPossibleUncheckedStates().size()==0){
            return currentNode;
        }


        //Basic version
        return extensionPhase(currentNode);
    }

    /**
     * Extension phase of the four phases of mcts
     * @param targetParentNode the Targeted node of the tree from which a child is selected
     * @return the child of targeted node
     */
    public Node extensionPhase(Node targetParentNode){
        // Adds one of the child of targetNode
        State state;

        //If the bot started the last trick and the opponent didnt play briscola or the same suit card, then we discard all the cards that
        //are not of the same suit.
        if(targetParentNode.getParentNode()!=null&&targetParentNode.getParentNode().getState().lastTrick!=null){
            state = targetParentNode.getState().getRandomChildState2(true, targetParentNode.getParentNode().getState().lastTrick[0],targetParentNode.getParentNode().getState().lastTrick[1]);
        }
        else state = targetParentNode.getState().getRandomChildState();


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
    public double simulationPhase(Node targetNode){
        //simulate couple and return average of normalized scores in game instead of (win = 1, draw = 0, lose = 0;

        double score = 0 ;
        int simulationCount = 2;
        for (int i = 0; i < simulationCount; i++) {
            State simulationState = new State(targetNode.getState().getBoardState().clone(), targetNode.getState().getRootPlayerNumber());
            simulationState.createAllPossibleStates();
            while (true) {
                if (simulationState.getBoardState().gameOver() || simulationState.getPossibleUncheckedStates().size() == 0) {
                    if (simulationState.getBoardState().getWinnerChickenDinner() == -1) {
                        score += 0;
                        break;
                    }
                    if (simulationState.getBoardState().getWinnerChickenDinner() == simulationState.getRootPlayerNumber()) {
                        score += 1;
                    }
                    break;
                } else {
                    simulationState = simulationState.getRandomChildStateSimulated();
                }

            }
        }
        return score/simulationCount;

    }

    /**
     * Backpropagation phase of the four phases of mcts
     * @param targetNode the node from which we backpropagate to the root 
     */
    public void backpropagationPhase(Node targetNode, double score){
        targetNode.getState().addToVisitCount();
        targetNode.getState().addWinScore(score);

        if(targetNode.getParentNode()==null){
            return;
        }
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
            } else if (node.getState().getScoreForState()/node.getState().getVisitCountForState() > highestValueNode.getState().getScoreForState()/highestValueNode.getState().getVisitCountForState()) {
                highestValueNode = node;
            }
        }
        return highestValueNode;
    }

}
