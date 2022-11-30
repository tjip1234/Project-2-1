package Game.Bots.MCTS;

import Game.Bots.Bot;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
     * Initialize Tree
     * @param board, an object representing the board
     * @return The tree
     */
    public Tree initializeTree(GameSession board){
        Tree tree = new Tree(board, board.currentPlayer);
        int x = tree.getRootNode().getState().getPossibleStates().size();
        for (int i = 0; i < x; i++) {
            Node node = extensionPhase(tree.getRootNode());
            backpropagationPhase(node,simulationPhase(node));
        }

        for (int i = 0; i < tree.getRootNode().getListOfChildren().size(); i++) {
            int y = tree.getRootNode().getListOfChildren().get(i).getState().getPossibleStates().size();
            for (int j = 0; j < y; j++) {
                Node node = extensionPhase(tree.getRootNode().getListOfChildren().get(i));
                backpropagationPhase(node,simulationPhase(node));
            }
        }
        for (int i = 0; i < tree.getRootNode().getListOfChildren().size(); i++) {
            for (int j = 0; j < tree.getRootNode().getListOfChildren().get(i).getListOfChildren().size(); j++) {
                int z = tree.getRootNode().getListOfChildren().get(i).getListOfChildren().get(j).getState().getPossibleStates().size();
                for (int j2 = 0; j2 < z ; j2++) {

                    Node node = extensionPhase(tree.getRootNode().getListOfChildren().get(i).getListOfChildren().get(j));
                    backpropagationPhase(node, simulationPhase(node));
                }
            }
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

        Tree tree = initializeTree(board);
        for (int i = 0; i < board.players[board.currentPlayer].getHand().size(); i++) {
            extensionPhase(tree.getRootNode());
        }
        while(iterationCount!=0){
            Node currentNode = tree.getRootNode();
            currentNode = selectionPhase(currentNode);
            backpropagationPhase(currentNode,simulationPhase(currentNode));

            iterationCount--;
        }
        //tree.saveTree(round++);
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
            //If it isn't bots turn choose random child
            if(currentNode.getState().getBoardState().currentPlayer!=currentNode.getState().rootPlayerNumber){
                currentNode = currentNode.getListOfChildren().get((int)(Math.random()*currentNode.getListOfChildren().size()));
                continue;
            }
            Node tempNode = UTC.findPossibleNode(currentNode,UTC_constant);
//            if((((double)tempNode.getState().getVisitCountForState()/(double)currentNode.getState().getVisitCountForState())>0.95&&currentNode.getState().getPossibleStates().size()>0)){
//                break;
//            }


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
        if(!targetParentNode.getState().canYouAddAnChildState()){
            return  targetParentNode;
        }
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
        double score= 0;
            State simulationState = targetNode.getState();

            while(true){
                if(simulationState.getBoardState().gameOver() || !simulationState.canYouAddAnChildState()){
                    if(simulationState.getBoardState().getWinnerChickenDinner() == simulationState.getRootPlayerNumber()){
                        State finalStateToExplore = simulationState;
                        //true if two players have the same score
                        if (Arrays.stream(simulationState.getBoardState().players).filter(
                                        c -> c.Score() == finalStateToExplore.getBoardState().players[finalStateToExplore.getRootPlayerNumber()].Score())
                                .count() > 1) {
                            score = 0.5;
                        } else {
                            score += 1;
                        }
                    } else {
                        score = 0;
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
