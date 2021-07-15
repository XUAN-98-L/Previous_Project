package Analyzer;

public class Edge {
    //Edge class receive two Node arguments.
    private Node nodeA;
    private Node nodeB;

    public Edge(Node nodeA, Node nodeB) {
        //Constructor (make a connection between two nodes)
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public Node getNodeA() {
        return this.nodeA;
    }

    public Node getNodeB() {
        return this.nodeB;
    }

    public boolean equals(Edge edge) {
        //Overwrite equals method and determine whether the connection is repeated
        return this.nodeA.equals(edge.nodeA) && this.nodeB.equals(edge.nodeB) || this.nodeA.equals(edge.nodeB) && this.nodeB.equals(edge.nodeA);
    }
}
