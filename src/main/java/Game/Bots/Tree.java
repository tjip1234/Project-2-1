package Game.Bots;

public class Tree {
    private Node rootNode;

    public Tree() {
        rootNode = new Node();
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
}
