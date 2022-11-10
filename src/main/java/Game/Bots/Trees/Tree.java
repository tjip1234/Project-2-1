package Game.Bots.Trees;

import Game.Bots.MCST.State;
import Game.Bots.Trees.Node;
import Game.GameSession;

public class Tree {
    private Node rootNode;


    public Tree(GameSession initialState, int rootPlayer){
        rootNode = new Node(new State(initialState, rootPlayer));
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
}
