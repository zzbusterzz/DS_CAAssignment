/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SceneBuildController;

import indexingprogram.IndexingProgram;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author 1
 */
public class Controller implements Initializable {

    private Desktop desktop = Desktop.getDesktop();
    
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
       // path = new TextField();
    }    

    @FXML
    private void locateFile(MouseEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file");
        
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(openButton.getScene().getWindow());
        if (file != null) {
           // openFile(file);
           path.setText(file.toString());
        }
    }
    
    
            
    private static void configureFileChooser(
        final FileChooser fileChooser) {      
            fileChooser.setTitle("Open text files");
            fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );                 
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
                //new FileChooser.ExtensionFilter("PNG", "*.png")
            );
    }
    
    
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                IndexingProgram.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}
