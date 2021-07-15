package sample;

import Analyzer.Operation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller {
    private static String nodeName = "";
    private static String nodeA = "";
    private static String nodeB = "";
    public Button printStatus;

    @FXML
    private TextField NodeA;

    @FXML
    private TextField NodeB;

    @FXML
    private Button getNodeDegree;

    @FXML
    private TextField NodeName;

    @FXML
    private Button readFile;

    @FXML
    private Button saveFile;

    @FXML
    private TextArea console;

    @FXML
    private Button addInteraction;

    @FXML
    private Button clean;

    @FXML
    void cleanConsole(ActionEvent event) {
        console.clear();
    }

    @FXML
    void readNetworkFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //Set the title
        fileChooser.setTitle("choose file");
        //Filter the type of input file (.txt format)
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TEXT","*.txt"));
        //Display the selection window to get the specific file
        File file =  fileChooser.showOpenDialog(new Stage());
        //Avoid NullPointException
        if(file == null){
            return;
        }

        //Print prompting message
        System.out.println(file.getAbsolutePath());
        console.appendText("Read file:" + file.getAbsolutePath()+"\n");
        Map<String, Integer> info = Operation.readNetworkFile(file.getAbsolutePath());
        if (info==null){//A read exception indicates that the file is not in a correct format. Return prompting message
            console.appendText("Wrong format in file,please correct\n");
        }else {
            console.appendText("Finished"+"\nsuccessful import interaction:"+info.get("success")+"\nrepeated interaction:"+info.get("repeated")+"\n");
            console.appendText(Operation.NetworkStatus());
        }

        //Use a Listener to transfer the text input (in textfield). (Before I used the Lisenter, I found that I needed to press Enter before clicking "add“ and "get", otherwise I cannot directly add or get node name.)
        NodeName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeName = newValue;
            }
        });
        NodeA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeA= newValue;
            }
        });
        NodeB.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nodeB = newValue;
            }
        });
    }

    @FXML
    void getNodeEdgeNum(ActionEvent event) {
        if(!Operation.checkNetwork()){   //Check whether the network is created (Chech whether txt file is entered)
            console.appendText("Please read txt file to build a Network\n");
            return;
        }
        System.out.println(nodeName);
        String edgeNum = Operation.getNodeEdgeNum(nodeName);
        if (edgeNum==null){
            console.appendText("Node not exist\n");
        }else {
            console.appendText("("+nodeName+")"+"Node degree:"+edgeNum+"\n");
        }
    }

    @FXML
    void addInteraction(ActionEvent event) {
        if(!Operation.checkNetwork()){
            console.appendText("Please read txt file to build a Network\n");
            return;
        }
        int i = Operation.addInteraction(nodeA,nodeB);
        if(i==1){
            console.appendText("success\n");
        }else if(i==0){
            console.appendText("interaction already exists\n");
        }else {
            console.appendText("node name can not be empty\n");
        }
    }

    @FXML
    void printInteractionList(ActionEvent event) {
        if(!Operation.checkNetwork()){
            console.appendText("Please read txt file to build a Network\n");
            return;
        }
        console.appendText(Operation.printInteractionList());
    }

    @FXML
    void printNetworkStatus(ActionEvent event) {
        if(!Operation.checkNetwork()){
            console.appendText("Please read txt file to build a Network\n");
            return;
        }
        console.appendText(Operation.NetworkStatus());
    }

    @FXML
    void writeNetworkFile(ActionEvent event) {
        if(!Operation.checkNetwork()){
            console.appendText("Please read txt file to build a Network\n");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        //Set the title
        fileChooser.setTitle("save file");
        //Filter the type of output file (.csv)
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Table","*.csv"));
        File file = fileChooser.showSaveDialog(new Stage());
        String filePath = file.getAbsolutePath();
        Operation.writeNetworkFile(filePath);
        console.appendText("success,path:" + filePath);
    }
}
