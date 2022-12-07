package Game.Bots.Trees;

import Game.Bots.MCTS.State;
import Game.GameSession;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Tree {
    private Node rootNode;


    public Tree(GameSession initialState, int rootPlayer){
        rootNode = new Node(new State(initialState, rootPlayer));
        rootNode.getState().createAllPossibleStates();
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public void saveTree(int round) throws IOException {
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCTS/dataMCTS.txt", true);
        PrintWriter out = new PrintWriter(file);
        String a = "";
        for (int i = 0; i < getRootNode().getListOfChildren().size(); i++) {
            a+=getRootNode().getListOfChildren().get(i).getState().getVisitCountForState()+", "+(getRootNode().getListOfChildren().get(i).getState().getScoreForState()/getRootNode().getListOfChildren().get(i).getState().getVisitCountForState())    +", ";
        }


        out.println(a);
        out.close();
    }

}
