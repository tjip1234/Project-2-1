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
        FileWriter file = new FileWriter("src/main/java/Game/Bots/MCST/testing.txt", true);
        PrintWriter out = new PrintWriter(file);
        StringBuilder a = new StringBuilder();
        int childCount = 0;
        a.append("Round: ").append(round).append(", ");
        for (Node child : rootNode.getListOfChildren()){
            a.append("Child: ").append(childCount++).append(", ");
            a.append("Score: ").append(child.getState().getScoreForState()).append(", ");
            a.append("VisitCount: ").append(child.getState().getVisitCountForState()).append(", ");
        }
        out.println(a);
        out.close();
    }

}
