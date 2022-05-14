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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class Inventory_add_product_Controller implements Initializable {

    @FXML
    private VBox addProductDialog;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInvQty;
    @FXML
    private TextField addProductMaxQty;
    @FXML
    private TextField addProductMinQty;
    @FXML
    private TextField addProductPriceCost;
    @FXML
    private Button addProductSaveBtn;
    @FXML
    private Button addProductCancelBtn;
    @FXML
    private TextField addProductID;
    @FXML
    private Button addProductAddPartBtn;
    @FXML
    private TextField addProductSearchTxt;
    @FXML
    private TableView addProductPartSearch;
    @FXML
    private TableView addProductPartContents;
    @FXML
    private Button addProductRemovePartBtn;
    @FXML
    private TableColumn addProductAllPartsID;
    @FXML
    private TableColumn addProductAllPartsName;
    @FXML
    private TableColumn addProductAllPartsInvQty;
    @FXML
    private TableColumn addProductAllPartsPriceCost;
    @FXML
    private TableColumn addProductPartContentsID;
    @FXML
    private TableColumn addProductPartContentsName;
    @FXML
    private TableColumn addProductPartContentsInvQty;
    @FXML
    private TableColumn addProductPartContentsPriceCost;
    @FXML
    private Label addProductWarning;
    
    private ObservableList<Part> addProdParts = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProductID.setText(Integer.toString(inventory.Inventory.getNewProdID()));
        
        addProductAllPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAllPartsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAllPartsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductPartSearch.setItems(inventory.Inventory.getAllParts());
        addProductPartSearch.setPlaceholder(new Label("No Parts Found"));
        
        addProductPartContentsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductPartContentsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductPartContentsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPartContentsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductPartContents.setItems(addProdParts);
        addProductPartContents.setPlaceholder(new Label("No Part Contents"));
    }    

    @FXML
    private void addProductSave(ActionEvent event) {
        try{
            //parse supplied data
            int id = parseInt(addProductID.getText());
            String name = addProductName.getText();
            int invQty = parseInt(addProductInvQty.getText());
            int maxQty = parseInt(addProductMaxQty.getText());
            int minQty = parseInt(addProductMinQty.getText());
            double priceCost = parseDouble(addProductPriceCost.getText());
            
            
            // check business logic input errors
            int error = inventory.Inventory.isValid(name, invQty, maxQty,minQty,priceCost,1,"",true);
            
            //if no detected input errors, save the part
            if( error == 0){
                addProductWarning.setText("");
                
                // add the product
                inventory.Inventory.addProduct(new Product(id, name, priceCost, invQty, maxQty, minQty, addProdParts));
                //increment prod ID sequence
                inventory.Inventory.setNewProdID();             
                //close window
                Stage stage = (Stage) addProductSaveBtn.getScene().getWindow();
                stage.close();
            }
            else{
                //set warning based on input error type
                addProductWarning.setText(inventory.Inventory.isValidText(error));
            }
        }
        catch (Exception e){
                System.out.println("error");
                addProductWarning.setText("Check supplied data types");
                //System.out.println(e);
        }
    }

    @FXML
    private void addProductCancel(ActionEvent event) {
        Stage stage = (Stage) addProductCancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addAssociatedPart(ActionEvent event) {
        addProdParts.add((Part)addProductPartSearch.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deleteAssociatedPart(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
            int id;
            if (addProductPartContents.getSelectionModel().getSelectedItem() != null){
                id = ((Part)addProductPartContents.getSelectionModel().getSelectedItem()).getId();
                addProdParts.remove((Part)addProductPartContents.getSelectionModel().getSelectedItem());
                inventory.Inventory.openMessageDialog(loader, "Delete Part Successful","Part (ID: " + id + ") removed from product.");
            }
            else{
                inventory.Inventory.openMessageDialog(loader, "Delete Part Failed","No part selected, delete operation failed");
            }
        }
        catch (Exception e){
            //stub
        }
        
        
    }
    
    //Searches parts by part ID and Part name (numeric input searched as ID and possible name substring)
    @FXML private void addProductSearchParts(){
        String searchQuery = addProductSearchTxt.getText().toUpperCase();
        int searchID = -1;
        try{
            searchID = Integer.parseInt(searchQuery);
        }
        catch (NumberFormatException e){
            //
        }
        final int finalSearchID = searchID;
        
        //If blank, reset table
        if (searchQuery.equals(""))
            addProductPartSearch.setItems(inventory.Inventory.getAllParts());
        //otherwise, search
        else{
            FilteredList<Part> filteredPartsTable = new FilteredList<>(inventory.Inventory.getAllParts(), part-> (part.getName().toUpperCase().contains(searchQuery) || part.getId() == finalSearchID));
            addProductPartSearch.setItems(filteredPartsTable);
        }
    }
}
