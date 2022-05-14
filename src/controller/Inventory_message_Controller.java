/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class Inventory_message_Controller implements Initializable {

    @FXML
    private Label messageTitle;
    @FXML
    private Label messageText;
    @FXML
    private Button messageOK;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void messageClose(ActionEvent event) {
        Stage stage = (Stage) messageOK.getScene().getWindow();
        stage.close();
    }
    
    public void setMessageTitle(String title){
        messageTitle.setText(title);
    }
    
    public void setMessageText(String text){
        messageText.setText(text);
    }
}
