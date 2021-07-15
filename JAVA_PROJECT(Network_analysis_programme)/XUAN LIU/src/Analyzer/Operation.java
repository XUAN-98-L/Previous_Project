package Analyzer;

import Analyzer.Node;
import Analyzer.Edge;
import Analyzer.Network;
import com.csvreader.CsvWriter;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class Operation {
    private static Network network;

    /*
    Read the input file (txt file)
    */
    public static Map<String, Integer> readNetworkFile(String filePath) throws IOException {
        Map<String, Integer> info = new HashMap();
        int success = 0;
        int repeated = 0;
        network = new Network();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String str;
        //Read the input file line by line and store the data in string format
        try {
            while ((str = br.readLine())!=null){
                System.out.println(str);
                //Truncate the string to get two node names
                String[] arrs = str.split("\t");
                //Calling addInteraction method to connect two nodes
                int i = addInteraction(arrs[0],arrs[1]);
                if(i==0){
                    repeated++;
                    continue;
                }else if(i==2){
                    return null;
                }
                //If the import is successful, record the number of successfully imported connection.
                success++;
            }
        }catch (Exception e){
            return null;
        }
        // Close the object of IO stream
        br.close();
        info.put("success",success);
        info.put("repeated",repeated);
        return info;
    }

    /*
    Add new interaction (edge)
    */
    public static int addInteraction(String nodeNameA,String nodeNameB){
        //Verify the input name is not empty (or null)
        if(nodeNameA==null||nodeNameA.equals("")||nodeNameB==null||nodeNameB.equals("")){
            return 2;
        }
        Node nodeA = null;
        Node nodeB = null;
        if (!network.getNodeMap().containsKey(nodeNameA)){
            nodeA = new Node(nodeNameA);
            network.addNode(nodeA);
        }else {
            //Node already exists, directly use the node
            nodeA = network.getNodeMap().get(nodeNameA);
        }
        //Same as nodeA
        if (!network.getNodeMap().containsKey(nodeNameB)){
            nodeB = new Node(nodeNameB);
            network.addNode(nodeB);
        }else {
            nodeB = network.getNodeMap().get(nodeNameB);
        }
        //Verify whether the connection already exists. If not, build a new connection.
        Edge edge = new Edge(nodeA,nodeB);
        if (!network.isEdgeExist(edge)){
            network.addEdge(edge);
        }else {
            return 0;
        }
        return 1;
    }

    /*
    Get the degree (edge number) of nodes
    */
    public static String getNodeEdgeNum(String nodeName){
        Node node = network.getNodeMap().get(nodeName);
        if(node==null){
            return null;
        }
        return String.valueOf(node.getEdgeNum());
    }

    /*
    Get the average degree
    */
    public static float getAverageDegree(){
        Map<String, Node> nodeMap = network.getNodeMap();
        float total = 0;
        //walk through the nodeMap
        for (Map.Entry<String, Node> entry:nodeMap.entrySet()){
            total = total + (float) entry.getValue().getEdgeNum();
        }
        float average = total/nodeMap.size();
        return average;
    }

    /*
    Get hubs (nodes with the highest degree)
    */
    public static Map<String,Object> getHubs(){
        Map<String, Node> nodeMap = network.getNodeMap();
        Map<String,Object> result = new HashMap<>();
        List<String> list = new ArrayList<>();

        //go through nodeMap to get the highest degree
        int highest = 0;
        for (Map.Entry<String, Node> entry:nodeMap.entrySet()){
            int degree = entry.getValue().getEdgeNum();
            if(degree > highest){
                highest = degree;
            }
        }
        //go through the nodeMap again to get the nodes with the highest degree (hub)
        for (Map.Entry<String, Node> entry:nodeMap.entrySet()){
            int degree = entry.getValue().getEdgeNum();
            if(degree == highest){
                list.add(entry.getKey());
            }
        }

        result.put("hubs",list);
        result.put("degree",highest);
        return result;
    }

    /*
    Generate files in CSV format
    */
    public static void writeNetworkFile(String filePath){
        boolean isSuccess = false;
        CsvWriter writer = null;
        FileOutputStream out = null;
        Map<String, Node> nodeMap = network.getNodeMap();

        //Store deta in List<String[]> format
        List<String[]> dataList = new ArrayList<String[]>();
        //Geader of the table
        String[] title = {"Degree","Nodes"};
        dataList.add(title);
        //Calling the getHubs method
        int highest = Integer.parseInt(getHubs().get("degree").toString());
        for (int i = 1;i<=highest;i++){
            int nodeNum = 0;
            for (Map.Entry<String, Node> entry:nodeMap.entrySet()){
                int degree = entry.getValue().getEdgeNum();
                if(degree == i){
                    nodeNum++;
                }
            }
            //Write a set of data
            String[] data = {String.valueOf(i),String.valueOf(nodeNum)};
            dataList.add(data);
        }

        //Output file, using csvWriter class
        try {
            out = new FileOutputStream(filePath, true);
            //If the output file is garbled，use "gbk" encoding format in windows system and "UTF-8" format in linux.
            writer = new CsvWriter(out, ',', Charset.forName("GBK"));
            for (String[] strs : dataList) {
                writer.writeRecord(strs);
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    Check whether the network object already declared.
     */
    public static boolean checkNetwork(){
        return network != null;
    }

    /*
    Network status
     */
    public static String NetworkStatus(){
        int nodeNum = network.getNodeMap().size();
        int edgeNum = network.getEdgeList().size();
        float average = getAverageDegree();
        Map<String,Object> hubsMap = getHubs();
        List<String> hubs = (List<String>) hubsMap.get("hubs");
        int degree = (int) hubsMap.get("degree");
        //Assemble the return message
        String status = "--------------Network status----------------\n"+
                "Node number:"+nodeNum+"\n"+
                "Interaction number:"+edgeNum+"\n"+
                "Average degree:"+average+"\n"+
                "Highest degree:"+degree+"\n"+
                "Hubs:"+hubs.toString()+"\n"+
                "---------------------------------------------\n";
        return status;
    }

    /*
    Ruturn the PPI network
     */
    public static String printInteractionList(){
        ArrayList<Edge> edgeList = network.getEdgeList();
        String result = "-------------Interaction list----------------\n";
        for(Edge edge:edgeList){
            String nodeA = edge.getNodeA().getNodeName();
            String nodeB = edge.getNodeB().getNodeName();
            result = result + nodeA + "\t" + nodeB +"\n";
        }
        result += "---------------------------------------------\n";
        return result;
    }
}
