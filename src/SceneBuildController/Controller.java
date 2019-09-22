/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneBuildController;

import com.sun.deploy.util.StringUtils;
import indexingprogram.IndexHolder;
import indexingprogram.WordDetail;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import sun.security.ssl.Debug;

/**
 * FXML Controller class
 *
 * @author 1
 */
public class Controller implements Initializable {

    private Alert alert;
    private IndexHolder indexdata;

    @FXML
    private TextField path;
    @FXML
    private Button openButton;
    @FXML
    private Button buildIndex;
    @FXML
    private TextField phraseInput;
    @FXML
    private Button testPhrase;
    @FXML
    private TextArea resultDisplay;

    private Stage primaryStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alert = new Alert(AlertType.NONE);
        indexdata = new IndexHolder();

    }

    @FXML
    private void locateFile(MouseEvent event) {
        primaryStage = (Stage) openButton.getScene().getWindow();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open files directory");
        File defaultDirectory;
        defaultDirectory = new File(System.getProperty("user.home"));
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            path.setText(selectedDirectory.toString());
        }
    }

    @FXML
    private void buildIndex(MouseEvent event) {
        String filePath = path.getText();//get the particular file or get the particular folder for files to look
        File folder = new File(filePath);
        if (filePath.contains(".pdf") || filePath.contains(".txt")) {//Not needed as we are using directory selector
            if(folder.isDirectory()){
                ScanFile(filePath);
            } else{
                alert.setAlertType(AlertType.WARNING);
                alert.setHeaderText("Not a valid file path");
                // show the dialog 
                alert.show();
            }
        } else {//Get all the pdf and txt files in that path
            if(folder.isDirectory()){
                String[] files = folder.list();
                for (String file : files) {
                    if (file.contains(".pdf") || file.contains(".txt")) {
                        ScanFile(filePath + "/" + file);
                    }
                }
            } else{
                alert.setAlertType(AlertType.WARNING);
                alert.setHeaderText("Not a valid file/folder path");

                // show the dialog 
                alert.show();
            }
        }
    }

    void ScanFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            System.out.println("file exists, and it is a file");
            String ext = filePath.substring(filePath.lastIndexOf("."));
            Path path = Paths.get(filePath);

            if (ext.equalsIgnoreCase(".pdf")) {
                PDDocument pdDoc = null;
                PDFTextStripper pdfStripper;
                String parsedText;
                //check if file is pdf
                try {
                    pdDoc = PDDocument.load(file);
                    pdfStripper = new PDFTextStripper();
                    parsedText = pdfStripper.getText(pdDoc);

                    int wordCount = 0;
                    String[] lineString = parsedText.split("\r\n");
                    for (int i = 0; i < lineString.length; i++)//here i is our line number
                    {
                        String[] splitSpace = lineString[i].split(" ");
                        for (int j = 0; j < splitSpace.length; j++) {
                            indexdata.AddIndex(path.getFileName().toString(), splitSpace[j], new WordDetail(wordCount, i + 1));
                            wordCount++;
                        }
                    }

                    Debug.println("word count", wordCount + "");
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (pdDoc != null) {
                            pdDoc.close();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                Scanner sc;
                try {
                    sc = new Scanner(file);
                    int wordCount = 0;
                    int lineNumber = 1;
                    String[] s;
                    while (sc.hasNextLine()) {
                        s = sc.nextLine().split(" ");
                        for (int i = 0; i < s.length; i++) {
                            indexdata.AddIndex(path.getFileName().toString(), s[i], new WordDetail(wordCount, lineNumber));
                            wordCount++;
                        }
                        lineNumber++;
                    }

                    Debug.println("word count", wordCount + "");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            alert.setAlertType(AlertType.WARNING);
            alert.setHeaderText("Not a valid file type");

            // show the dialog 
            alert.show();
        }
    }

    @FXML
    private void checkPhraase(MouseEvent event) {
        String phrase = phraseInput.getText();
        if (phrase == null || phrase.equalsIgnoreCase("")) {
            resultDisplay.setText("Empty Phrase");
        } else {
            //Todo: calculate the pharase
            List<Object> phraseValue = indexdata.CheckForPhrase(phrase);
            int occuranceCount = 0;
            int returnType = (Integer) phraseValue.get(0);//0- false 1-true 2-same mini word occuring multiple times in same word
            if (returnType > 0) {
                String s = "";

                if (returnType == 2) {
                    Pattern p = Pattern.compile(phrase.toLowerCase());
                    HashMap<String, HashMap<String, List<WordDetail>>> map = (HashMap<String, HashMap<String, List<WordDetail>>>) phraseValue.get(1);//get the result hashmap
                    WordDetail detail;
                    for (Map.Entry<String, HashMap<String, List<WordDetail>>> entry : map.entrySet()) {
                        s += "\n\nFile : " + entry.getKey() + "\n";
                        for (Map.Entry<String, List<WordDetail>> entryChild : entry.getValue().entrySet()) {
                            String key = entryChild.getKey();
                            List<WordDetail> builtIndex = entryChild.getValue();
                            for (int i = 0; i < builtIndex.size(); i++) {

                                int j = 0;
                                Matcher m = p.matcher(key.toLowerCase());
                                while (m.find()) {
                                    j++;
                                }
                                occuranceCount += j;
                                detail = builtIndex.get(i);
                                s += "Index - " + detail.getIndex() + ", Line no - " + detail.getLineNumber() + "\n";
                            }
                        }
                    }
                } else {
                    HashMap<String, List<WordDetail>> map = (HashMap<String, List<WordDetail>>) phraseValue.get(1);//get the result hashmap
                    WordDetail detail;

                    for (Map.Entry<String, List<WordDetail>> entry : map.entrySet()) {
                        s += "\n\nFile : " + entry.getKey() + "\n";
                        List<WordDetail> builtIndex = entry.getValue();
                        for (int i = 0; i < builtIndex.size(); i++) {
                            occuranceCount++;

                            detail = builtIndex.get(i);
                            if (i == builtIndex.size() - 1) {
                                s += "Index - " + detail.getIndex() + ", Line no - " + detail.getLineNumber();
                            } else {
                                s += "Index - " + detail.getIndex() + ", Line no - " + detail.getLineNumber() + "\n";
                            }
                        }
                    }
                }
                resultDisplay.setText("Phrase occured : " + occuranceCount + "\nPhrase found at following :" + s);
            } else {
                resultDisplay.setText("Phrase not found");
            }
        }
    }
}
