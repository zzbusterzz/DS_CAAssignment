/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneBuildController;

import indexingprogram.IndexHolder;
import indexingprogram.WordDetail;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        alert = new Alert(AlertType.NONE);
        indexdata = new IndexHolder();
    }

    @FXML
    private void locateFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file");

        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(openButton.getScene().getWindow());
        if (file != null) {
            path.setText(file.toString());
        }
    }

    @FXML
    private void buildIndex(MouseEvent event) {
        String filePath = path.getText();
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            System.out.println("file exists, and it is a file");

            String ext = filePath.substring(filePath.lastIndexOf("."));
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
                    for(int i = 0; i < lineString.length; i++)//here i is our line number
                    {
                        String[] splitSpace = lineString[i].split(" ");
                        for (int j = 0; j < splitSpace.length; j++) {
                            indexdata.AddIndex(splitSpace[j], new WordDetail(wordCount, i+1));
                            wordCount++;
                        }
                    }
                    

                    Debug.println("word count", wordCount + "");
                    //System.out.println(parsedText.replaceAll("[^A-Za-z0-9. ]+", ""));
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
                            indexdata.AddIndex(s[i], new WordDetail(wordCount, lineNumber));
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

            if ((boolean) phraseValue.get(0)) {
                String s = "";
                WordDetail detail;
                for (int i = 1; i < phraseValue.size(); i++) {
                    detail = (WordDetail) phraseValue.get(i);
                    if (i == phraseValue.size() - 1) {
                        s += "Index - "+ detail.getIndex() + ", Line no - "+ detail.getLineNumber();
                    } else {
                        s += "Index - " + detail.getIndex() + ", Line no - "+ detail.getLineNumber() + "\n";
                    }
                }

                resultDisplay.setText("Phrase occurend : " + (phraseValue.size() - 1) + "\nPhrase found at following :\n" + s);
            } else {
                resultDisplay.setText("Phrase not found");
            }
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Open text files");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
    }
}