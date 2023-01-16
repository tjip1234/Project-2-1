package Game.Bots.MCTS;

import Game.Bots.Bot;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MCTS3_bot extends Bot {
    private int round;
    private double UTC_constant;
    private int iterationCount;

    public MCTS3_bot(int iterationCount, double UTC_constant){
        this.iterationCount = iterationCount;
        this.UTC_constant = UTC_constant;
    }


    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) throws IOException {
        return findCardToPlay(simulationSession.get(), iterationCount);
    }
    /**
     * recursive part of initializing the tree
     * @param parent, the node for which the process is aplied
     * @param recursionCounter, how many iterations left
     */
    private void recursionTreeInitialization(Node parent, int  recursionCounter, int howManyMax){
        if(recursionCounter==0){
            return;
        }
        int x = parent.getState().getPossibleUncheckedStates().size();
        for (int i = 0; (i < x)&&(i < howManyMax); i++) {
            Node node = extensionPhase(parent);
            backpropagationPhase(node,simulationPhase(node));
            recursionTreeInitialization(node, recursionCounter-1, howManyMax);
        }

    }

    /**
     * Initialize Tree
     * @param board, an object representing the board
     * @return The tree
     */
    public Tree initializeTree(GameSession board, int recursionCounter){
        Tree tree = new Tree(board, board.currentPlayer);
        tree.getRootNode().getState().createAllPossibleStates();
        int x = tree.getRootNode().getState().getPossibleUncheckedStates().size();

        for (int i = 0; i < x; i++) {
            Node node = extensionPhase(tree.getRootNode());
            backpropagationPhase(node,simulationPhase(node));
            recursionTreeInitialization(node, recursionCounter-1, 25);
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
    private Node selectionPhase(Node rootNode, int iterationCount){
        // depth =2 all children
        // depth >2 some heuristic

        Node currentNode = rootNode;

        int count = 0;
        //Goes through fully explored layers of the tree
        //while (currentNode.getState().getPossibleStates().size() <2) {
        //    Node tempNode = UTC.findPossibleNode(currentNode);
        //    if(tempNode == null){break;}
        //    currentNode = tempNode;


        //}

        //OG selection with modification
        // 26.7% on 1000 games with 2000 iteration against RL
        // while(currentNode.getListOfChildren().size()>0){
        //  if(((currentNode.getState().getPossibleStates().size()>0)&&(Math.random()<0.005))){
        //      break;
        //  }
        //  Node tempNode = UTC.findPossibleNode(currentNode);
        //  if(tempNode == null){break;}
        //  currentNode = tempNode;
        // }

        //OG selection with modification2
        // 36.2% on 1000 games with 2000 iteration against RL when ammount = 0.95
        // 41% on 1000 games with 2000 iteration against RL when ammount = 0.95 with depth 2 tree

        while(currentNode.getListOfChildren().size()>0){
            //5% chance to stop here
            if(((currentNode.getState().getPossibleUncheckedStates().size()>0)&&(Math.random()<0.05))){
                break;
            }

            //if Bot turn, choose random
            if(currentNode.getState().getBoardState().currentPlayer!=currentNode.getState().rootPlayerNumber){
                int holder = currentNode.getListOfChildren().size()+currentNode.getState().getPossibleUncheckedStates().size();
                Random random = new Random();
                if(random.nextInt(holder)>currentNode.getListOfChildren().size()){
                    break;
                }
                currentNode = currentNode.getListOfChildren().get((int)(Math.random()*currentNode.getListOfChildren().size()));
                //System.out.println("RandomNode "+currentNode.getState().getVisitCountForState()+" Its parent "+currentNode.getParentNode().getState().getVisitCountForState());

                continue;
            }


            currentNode = UTC.findPossibleNode(currentNode,UTC_constant);
        }
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
    private Node extensionPhase(Node targetParentNode){
        // Adds one of the child of targetNode

        State state = targetParentNode.getState().getRandomChildState();
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

        double score = 0 ;
        int simulationCount = 1;
        for (int i = 0; i < simulationCount; i++) {


            State simulationState = new State(targetNode.getState().getBoardState().clone(), targetNode.getState().getRootPlayerNumber());
            simulationState.createAllPossibleStates();
            while (true) {
                if (simulationState.getBoardState().gameOver() || simulationState.getPossibleUncheckedStates().size() == 0) {
                    if (simulationState.getBoardState().getWinnerChickenDinner() == -1) {
                        score += 0.5;
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
    private void backpropagationPhase(Node targetNode, double score){
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

    public Node getRoute(GameSession board, int iterationCount) {
        Tree tree = initializeTree(board,1 );

        while(iterationCount!=0){
            Node currentNode = tree.getRootNode();
            currentNode = selectionPhase(currentNode, iterationCount);
            backpropagationPhase(currentNode,simulationPhase(currentNode));

            iterationCount--;
        }
        return tree.getRootNode();
    }


    
    
}
