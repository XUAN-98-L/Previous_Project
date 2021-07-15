package Analyzer;

public class Node {
    //Members of Node class include the node name and the number of connections (edges)
    private String nodeName;
    private int edgeNum;

    //Constructor
    public Node(){
        this.nodeName = "";
        this.edgeNum = 0;
    }

    public Node(String nodeName){
        this.nodeName = nodeName;
        this.edgeNum = 0;
    }

    //getter&setter
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getEdgeNum() {
        return edgeNum;
    }

    public void setEdgeNum(int edgeNum) {
        this.edgeNum = edgeNum;
    }

    //override equals method. If two nodes have the same name, two node objects are considered to be the same.
    public boolean equals(Node node){
        return this.nodeName.equals(node.nodeName);
    }
}
