package Game.Bots.MCST;

import Game.Bots.Bot;
import Game.Bots.Trees.Node;
import Game.Bots.Trees.Tree;
import Game.Cards.Card;
import Game.GameSession;

import java.util.Arrays;
import java.util.List;

public class MCST2_bot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) {
        return findCardToPlay(simulationSession.get(), 1000);
    }
    public double simulate(State simulationState, int rootPlayerNumber ){

        double score;
        while(true){
            if(simulationState.getBoardState().gameOver()){
                if(simulationState.getBoardState().getWinnerChickenDinner() == rootPlayerNumber){
                    State finalStateToExplore = simulationState;
                    //true if two players have the same score
                    if (Arrays.stream(simulationState.getBoardState().players).filter(
                                    c -> c.Score() == finalStateToExplore.getBoardState().players[rootPlayerNumber].Score())
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
            simulationState=(simulationState.getRandomChildState());
        }
        return score;
    }
    public Tree initializeTree(GameSession board){
        Tree tree = new Tree(board, board.currentPlayer);
        Node rootNode = tree.getRootNode();
        addingAllChildNode(2,rootNode);
        return tree;
    }
    public void addingAllChildNode(int depth, Node currentNode){
        if(depth == 0){return;}
        List<State> possibleStates = currentNode.getState().createAllPossibleStates();

        for (State possibleState : possibleStates) {
            Node newNode = new Node(possibleState);
            newNode.setParentNode(currentNode);
            currentNode.getListOfChildren().add(newNode);
            addingAllChildNode(depth-1,newNode);
        }

    }
    public Node selection(Node rootNode){
        int count = 0;
        while (rootNode.getListOfChildren().size() != 0) {
            count = rootNode.getState().getVisitCountForState();
            rootNode = UTC.findPossibleNode(rootNode);
        }
        return rootNode;
    }


    public Card findCardToPlay(GameSession board, int iterationCount) {
        Tree tree = initializeTree(board);
        //TODO Possible to do time-based instead of iteration based
        long starttime = System.currentTimeMillis();
        int  iterationCount2 = 0;
        while (System.currentTimeMillis()-starttime<200) {
            iterationCount2++;
            // Selection
            Node currentNode = selection(tree.getRootNode());

            // Extension
            //TODO Do we add all possible children?
            if (!currentNode.getState().getBoardState().gameOver()) {
                List<State> possibleStates = currentNode.getState().createAllPossibleStates();
                for (State possibleState : possibleStates) {
                    Node newNode = new Node(possibleState);
                    newNode.setParentNode(currentNode);
                    currentNode.getListOfChildren().add(newNode);
                }
            }

            // Simulation
            State simulationState = currentNode.getState();
            double score;
            //TODO getting a draw
            score = simulate(simulationState, tree.getRootNode().getState().rootPlayerNumber);

            //Backpropagation
            backPropagate(currentNode, score);
            iterationCount--;
        }
        var winner = getWinningNode(tree.getRootNode()).getState();
        return winner.getCardPlayed();
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

    public static void backPropagate(Node node, double Score){
        node.getState().addToVisitCount();

        if(node.getParentNode()==null){
            return;
        }

        node.getParentNode().getState().addWinScore(Score);
        backPropagate(node.getParentNode(), Score);
    }
}
