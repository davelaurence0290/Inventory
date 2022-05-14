/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import inventory.Part;
import inventory.Product;
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
public class Inventory_modify_product_Controller implements Initializable {

    @FXML
    private VBox modProductDialog;
    @FXML
    private TextField modProductName;
    @FXML
    private TextField modProductInvQty;
    @FXML
    private TextField modProductMaxQty;
    @FXML
    private TextField modProductMinQty;
    @FXML
    private TextField modProductPriceCost;
    @FXML
    private Button modProductSaveBtn;
    @FXML
    private Button modProductCancelBtn;
    @FXML
    private TextField modProductID;
    @FXML
    private Button modProductAddPartBtn;
    @FXML
    private TextField modProductSearchTxt;
    @FXML
    private TableView modProductPartSearch;
    @FXML
    private TableView modProductPartContents;
    @FXML
    private Button modProductRemovePartBtn;
    @FXML
    private TableColumn modProductAllPartsID;
    @FXML
    private TableColumn modProductAllPartsName;
    @FXML
    private TableColumn modProductAllPartsInvQty;
    @FXML
    private TableColumn modProductAllPartsPriceCost;
    @FXML
    private TableColumn modProductPartContentsID;
    @FXML
    private TableColumn modProductPartContentsName;
    @FXML
    private TableColumn modProductPartContentsInvQty;
    @FXML
    private TableColumn modProductPartContentsPriceCost;
    @FXML
    private Label modProductWarning;

    private int modIndex;
    private ObservableList<Part> modProdParts = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modProductAllPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductAllPartsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductAllPartsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProductPartSearch.setItems(inventory.Inventory.getAllParts());
        modProductPartSearch.setPlaceholder(new Label("No Parts Found"));
        
        modProductPartContentsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductPartContentsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductPartContentsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductPartContentsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        modProductPartContents.setItems(modProdParts);
        modProductPartContents.setPlaceholder(new Label("No Part Contents"));
        
    }    

    @FXML
    private void modProductSave(ActionEvent event) {
        try{
            //parse supplied data
            int id = parseInt(modProductID.getText());
            String name = modProductName.getText();
            int invQty = parseInt(modProductInvQty.getText());
            int maxQty = parseInt(modProductMaxQty.getText());
            int minQty = parseInt(modProductMinQty.getText());
            double priceCost = parseDouble(modProductPriceCost.getText());
            
            // check business logic input errors
            int error = inventory.Inventory.isValid(name, invQty, maxQty,minQty,priceCost,1,"",true);
            
            //if no detected input errors, save the part
            if( error == 0){
                modProductWarning.setText("");
                
                // mod the product
                inventory.Inventory.updateProduct(modIndex,new Product(id, name, priceCost, invQty, maxQty, minQty, modProdParts));     
                //close window
                Stage stage = (Stage) modProductSaveBtn.getScene().getWindow();
                stage.close();
            }
            else{
                //set warning based on input error type
                modProductWarning.setText(inventory.Inventory.isValidText(error));
            }
        }
        catch (Exception e){
                System.out.println(e);
                modProductWarning.setText("Check supplied data types");
                //System.out.println(e);
        }
    }

    @FXML
    private void modProductCancel(ActionEvent event) {
        Stage stage = (Stage) modProductCancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addAssociatedPart(ActionEvent event) {
        modProdParts.add((Part)modProductPartSearch.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deleteAssociatedPart(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
            int id;
            if (modProductPartContents.getSelectionModel().getSelectedItem() != null){
                id = ((Part)modProductPartContents.getSelectionModel().getSelectedItem()).getId();
                modProdParts.remove((Part)modProductPartContents.getSelectionModel().getSelectedItem());
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
    
    /**
     * @param id the id to set
     */
    public void setModProductId(int id){
        modProductID.setText(Integer.toString(id));
    }
    
    /**
     * @param name the part name to set
     */
    public void setModProductName(String name){
        modProductName.setText(name);
    }
    
    /**
     * @param qty the stock qty to set
     */
    public void setModProductInvQty(int qty){
        modProductInvQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param qty the max qty to set
     */
    public void setModProductMaxQty(int qty){
        modProductMaxQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param qty the min qty to set
     */
    public void setModProductMinQty(int qty){
        modProductMinQty.setText(Integer.toString(qty));
    }
    
    /**
     * @param price the price to set
     */
    public void setModProductPriceCost(double price){
        modProductPriceCost.setText(Double.toString(price));
    }
    
    /**
     * @param index the index to set. The index of the selected part in the list of all parts.
     */
    public void setModIndex(int index){
        modIndex = index;
    }
    
    /**
     * @param parts the list of parts associated with product to modify.
     */
    public void setModParts(ObservableList<Part> parts){
        /**
         * This line ended up being the source of a bug due to the way I originally had written it:
         * modProdParts = parts;            
         * 
         * Since observable lists use the .addAll() method to add multiple parts, assignment through '=' was not working,
         * and the associated parts of the modified product would not be displayed. This was hard to track down because
         * there are multiple methods to trace which lead to this line, and it took a while to discover which 'link' was
         * causing empty parts lists. At one point during refactoring, I even wound up with NullPointerExceptions because
         * the observable list for parts was not properly instantiated. Luckily, I finally figured it out and corrected 
         * the method to be the line below, and used it in a few other places.
         *             
        */
        modProdParts.addAll(parts);
    }
    
    //Searches parts by part ID and Part name (numeric input searched as ID and possible name substring)
    @FXML private void modProductSearchParts(){
        String searchQuery = modProductSearchTxt.getText().toUpperCase();
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
            modProductPartSearch.setItems(inventory.Inventory.getAllParts());
        //otherwise, search
        else{
            FilteredList<Part> filteredPartsTable = new FilteredList<>(inventory.Inventory.getAllParts(), part-> (part.getName().toUpperCase().contains(searchQuery) || part.getId() == finalSearchID));
            modProductPartSearch.setItems(filteredPartsTable);
        }
    }
    
}
