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
public class Inventory_modify_part_Controller implements Initializable {

    @FXML
    private VBox modPartDialog;
    @FXML
    private RadioButton modPartInHouse;
    @FXML
    private ToggleGroup partType;
    @FXML
    private RadioButton modPartOutsourced;
    @FXML
    private Label modPartUniqueLabel;
    @FXML
    private TextField modPartName;
    @FXML
    private TextField modPartInvQty;
    @FXML
    private TextField modPartMaxQty;
    @FXML
    private TextField modPartMinQty;
    @FXML
    private TextField modPartPriceCost;
    @FXML
    private TextField modPartUniqueAttr;
    @FXML
    private Button modPartSaveBtn;
    @FXML
    private Button modPartCancelBtn;
    @FXML
    private TextField modPartID;
    @FXML
    private Label modPartWarning;
    
    private int modIndex;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void setUniqueAttrText(){
        if (modPartInHouse.isSelected()){
            modPartUniqueLabel.setText("Machine ID");
            modPartUniqueAttr.setPrefWidth(70.0);
        }
        else{
            modPartUniqueLabel.setText("Company");
            modPartUniqueAttr.setPrefWidth(200.0);
        }
    }
    
    @FXML
    private void modPartSave(ActionEvent event) {
        try{
            //parse supplied data
            int id = parseInt(modPartID.getText());
            String name = modPartName.getText();
            int invQty = parseInt(modPartInvQty.getText());
            int maxQty = parseInt(modPartMaxQty.getText());
            int minQty = parseInt(modPartMinQty.getText());
            double priceCost = parseDouble(modPartPriceCost.getText());
            /* only need to check if unique is int if In house part. Otherwise
            string is fine */
            int machineId = 0;
            String company = "";
            if(modPartInHouse.isSelected())
                machineId = parseInt(modPartUniqueAttr.getText());
            else
                company = modPartUniqueAttr.getText();
            
            // check business logic input errors
            int error = inventory.Inventory.isValid(name, invQty, maxQty,minQty,priceCost,machineId,company,modPartInHouse.isSelected());
            
            //if no detected input errors, save the part
            if( error == 0){
                modPartWarning.setText("");
                                
                if (modPartInHouse.isSelected()){
                    inventory.Inventory.updatePart(modIndex,new InHouse(id, name, priceCost, invQty, minQty, maxQty, machineId));
                }
                else{
                    inventory.Inventory.updatePart(modIndex,new Outsourced(id, name, priceCost, invQty, minQty, maxQty, company));
                }
                
                //close window
                Stage stage = (Stage) modPartSaveBtn.getScene().getWindow();
                stage.close();
            }
            else{
                //set warning based on input error type
                modPartWarning.setText(inventory.Inventory.isValidText(error));
            }
        }
        catch (Exception e){
                modPartWarning.setText("Check supplied data types");
                //System.out.println(e);
        }
    }

    @FXML
    private void modPartCancel(ActionEvent event) {
        modPartWarning.setText("");
        Stage stage = (Stage) modPartCancelBtn.getScene().getWindow();
        stage.close();
    }
    
    /**
     * @param id the id to set
     */
    public void setModPartId(int id){
        modPartID.setText(Integer.toString(id));
    }
    
    /**
     * @param name the part name to set
     */
    public void setModPartName(String name){
        modPartName.setText(name);
    }
    
    /**
     * @param qty the stock qty to set
     */
    public void setModPartInvQty(int qty){
        modPartInvQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param qty the max qty to set
     */
    public void setModPartMaxQty(int qty){
        modPartMaxQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param qty the min qty to set
     */
    public void setModPartMinQty(int qty){
        modPartMinQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param price the price to set
     */
    public void setModPartPriceCost(double price){
        modPartPriceCost.setText(Double.toString(price));
    }
    
    /**
     * @param attr the value of the unique attribute to set. Either Machine ID or Company name.
     */
    public void setModPartUniqueAttr(String attr){
        modPartUniqueAttr.setText(attr);
    }
    
    /**
     * @param type the desired type of the modified part
     */
    public void setModPartType(String type){
        if (type.equals("InHouse"))
            modPartInHouse.setSelected(true);
        else
            modPartOutsourced.setSelected(true);
        //ensure correct field label and width
        setUniqueAttrText();
    }
    
    /**
     * @param index the index to set. The index of the selected part in the list of all parts.
     */
    public void setModIndex(int index){
        modIndex = index;
    }
    
}
