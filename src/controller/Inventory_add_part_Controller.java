/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import inventory.*;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class Inventory_add_part_Controller implements Initializable {

    @FXML
    private VBox addPartDialog;
    @FXML
    private RadioButton addPartInHouse;
    @FXML
    private ToggleGroup partType;
    @FXML
    private RadioButton addPartOutsourced;
    @FXML
    private Label addPartUniqueLabel;
    @FXML
    private TextField addPartName;
    @FXML
    private TextField addPartInvQty;
    @FXML
    private TextField addPartMaxQty;
    @FXML
    private TextField addPartMinQty;
    @FXML
    private TextField addPartPriceCost;
    @FXML
    private TextField addPartUniqueAttr;
    @FXML
    private Button addPartSaveBtn;
    @FXML
    private Button addPartCancelBtn;
    @FXML
    private TextField addPartID;
    @FXML
    private Label addPartWarning;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addPartID.setText(Integer.toString(inventory.Inventory.getNewPartID()));
    }    

    
    
    @FXML
    private void setUniqueAttrText(){
        if (addPartInHouse.isSelected()){
            addPartUniqueLabel.setText("Machine ID");
            addPartUniqueAttr.setPrefWidth(70.0);
        }
        else{
            addPartUniqueLabel.setText("Company");
            addPartUniqueAttr.setPrefWidth(200.0);
        }
            
    }
    
    @FXML
    private void addPartSave(ActionEvent event) {
        try{
            //parse supplied data
            int id = parseInt(addPartID.getText());
            String name = addPartName.getText();
            int invQty = parseInt(addPartInvQty.getText());
            int maxQty = parseInt(addPartMaxQty.getText());
            int minQty = parseInt(addPartMinQty.getText());
            double priceCost = parseDouble(addPartPriceCost.getText());
            /* only need to check if unique is int if In house part. Otherwise
            string is fine */
            int machineId = 0;
            String company = "";
            if(addPartInHouse.isSelected())
                machineId = parseInt(addPartUniqueAttr.getText());
            else
                company = addPartUniqueAttr.getText();
            
            // check business logic input errors
            int error = inventory.Inventory.isValid(name, invQty, maxQty,minQty,priceCost,machineId,company,addPartInHouse.isSelected());
            
            //if no detected input errors, save the part
            if( error == 0){
                addPartWarning.setText("");
                if (addPartInHouse.isSelected()){
                    inventory.Inventory.addPart(new InHouse(id, name, priceCost, invQty, minQty, maxQty, machineId));
                }
                else{
                    inventory.Inventory.addPart(new Outsourced(id, name, priceCost, invQty, minQty, maxQty, company));
                }
                //increment part ID sequence
                inventory.Inventory.setNewPartID();
                //close window
                Stage stage = (Stage) addPartSaveBtn.getScene().getWindow();
                stage.close();
            }
            else{
                //set warning based on input error type
                addPartWarning.setText(inventory.Inventory.isValidText(error));
            }
        }
        catch (Exception e){
                System.out.println("error");
                addPartWarning.setText("Check supplied data types");
                //System.out.println(e);
        }
    }

    @FXML
    private void addPartCancel(ActionEvent event) {
        addPartWarning.setText("");
        Stage stage = (Stage) addPartCancelBtn.getScene().getWindow();
        stage.close();
    }
}