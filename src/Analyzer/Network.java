package Analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Network {
    private static HashMap<String, Node> nodeMap;
    private static ArrayList<Edge> edgeList;

    public Network() {
        nodeMap = new HashMap();
        edgeList = new ArrayList();
    }

    public void addNode(Node node) {
        nodeMap.put(node.getNodeName(), node);
    }

    public void addEdge(Edge edge) {
        edgeList.add(edge);
        Node nodeA = edge.getNodeA();
        Node nodeB = edge.getNodeB();
        //Meanwhile, update the number of edges to the node
        nodeA.setEdgeNum(nodeA.getEdgeNum() + 1);
        nodeB.setEdgeNum(nodeB.getEdgeNum() + 1);

    }

    public boolean isEdgeExist(Edge edge) {
        if (!edgeList.isEmpty()) {
            Iterator var2 = edgeList.iterator();

            while(var2.hasNext()) {
                Edge e = (Edge)var2.next();
                if (edge.equals(e)) {
                    return true;
                }
            }
        }

        return false;
    }

    public HashMap<String, Node> getNodeMap() {
        return nodeMap;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }
}
