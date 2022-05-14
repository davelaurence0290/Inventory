/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import inventory.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class InventoryMain implements Initializable {

    @FXML
    private TextField mainPartSearch;
    @FXML
    private Button mainAddPartBtn;
    @FXML
    private Button mainModPartBtn;
    @FXML
    private Button mainDeletePartBtn;
    @FXML
    private TextField mainProdSearch;
    @FXML
    private Button mainAddProductBtn;
    @FXML
    private Button mainModProductBtn;
    @FXML
    private Button mainDeleteProductBtn;
    @FXML
    private Button mainExitBtn;
    @FXML
    public TableView allPartsTable;
    @FXML
    public TableView allProductsTable;
    @FXML
    private TableColumn allPartsID;
    @FXML
    private TableColumn allPartsName;
    @FXML
    private TableColumn allPartsInvQty;
    @FXML
    private TableColumn allPartsPriceCost;
    @FXML
    private TableColumn allProductsID;
    @FXML
    private TableColumn allProductsName;
    @FXML
    private TableColumn allProductsInvQty;
    @FXML
    private TableColumn allProductsPriceCost;
    @FXML
    private AnchorPane mainScene;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsTable.setItems(inventory.Inventory.getAllParts());
        
        allProductsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allProductsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allProductsInvQty.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allProductsPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        allProductsTable.setItems(inventory.Inventory.getAllProducts());
        
        allPartsTable.setPlaceholder(new Label("No Parts Found"));
        allProductsTable.setPlaceholder(new Label("No Products Found"));
    }    

    @FXML
    //Searches parts by part ID and Part name (numeric input searched as ID and possible name substring)
    private void searchParts(ActionEvent event) {
        String searchQuery = mainPartSearch.getText().toUpperCase();
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
            allPartsTable.setItems(inventory.Inventory.getAllParts());
        //otherwise, search
        else{
            FilteredList<Part> filteredPartsTable = new FilteredList<>(inventory.Inventory.getAllParts(), part-> (part.getName().toUpperCase().contains(searchQuery) || part.getId() == finalSearchID));
            allPartsTable.setItems(filteredPartsTable);
        }
    }

    @FXML
    private void addPart(ActionEvent event) {
        
        try{
            Stage addPartStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/inventory_add_part.fxml"));            
            Scene addPartScene = new Scene(root);
            addPartStage.setScene(addPartScene);
            addPartStage.setTitle("Add Part");
            addPartStage.show();
        }
        catch(IOException e){
            System.out.println("didn't work\n" + e);
        }
    }

    @FXML
    private void modifyPart(ActionEvent event) {
        try{
            FXMLLoader loader;
            if (allPartsTable.getSelectionModel().getSelectedItem() != null){
                loader = new FXMLLoader(getClass().getResource("../view/inventory_modify_part.fxml"));
            
                Parent root = loader.load();
                Inventory_modify_part_Controller controller = loader.getController();

                Part selectedPart = (Part)allPartsTable.getSelectionModel().getSelectedItem();

                controller.setModPartId(selectedPart.getId());
                controller.setModPartName(selectedPart.getName());
                controller.setModPartInvQty(selectedPart.getStock());
                controller.setModPartMaxQty(selectedPart.getMax());
                controller.setModPartMinQty(selectedPart.getMin());
                controller.setModPartPriceCost(selectedPart.getPrice());

                int modIndex = inventory.Inventory.getPartIndex(selectedPart);
                controller.setModIndex(modIndex);

                //find out what kind of part it is, use the right method to populate unique attr text box.
                InHouse inHousePart;
                Outsourced outsourcedPart;
                if (allPartsTable.getSelectionModel().getSelectedItem() instanceof InHouse) {
                    inHousePart = (InHouse)allPartsTable.getSelectionModel().getSelectedItem();
                    controller.setModPartUniqueAttr(Integer.toString(inHousePart.getMachineId()));
                    controller.setModPartType("InHouse");
                }
                else{
                    outsourcedPart = (Outsourced)allPartsTable.getSelectionModel().getSelectedItem();
                    controller.setModPartUniqueAttr(outsourcedPart.getCompanyName());
                    controller.setModPartType("Outsourced");
                }

                Scene modPartScene = new Scene(root);
                Stage modPartStage = new Stage();
                modPartStage.setScene(modPartScene);
                modPartStage.setTitle("Modify Part");
                modPartStage.show();
            }
            else{
                loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
                inventory.Inventory.openMessageDialog(loader, "Modify Part Failed","No part selected, modify operation failed");
            }
            
        }
        catch(IOException e){
            System.out.println("didn't work\n" + e);
        }
    }

    @FXML
    private void deletePart(ActionEvent event) {
        try{
            int id;
            Part target = (Part)allPartsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
            if (target != null){
                id = target.getId();
                inventory.Inventory.deletePart(target);
                inventory.Inventory.openMessageDialog(loader, "Delete Part Successful","Part (ID: " + id + ") deleted.");
            }
            else{
                inventory.Inventory.openMessageDialog(loader, "Delete Part Failed","No part selected, delete operation failed");
            }
        }
        catch (Exception e){
            //if loader fails to initialize...
        }
    }

    @FXML
    //Searches products by product ID and product name (numeric input searched as ID and possible name substring)
    private void searchProducts(ActionEvent event) {
        String searchQuery = mainProdSearch.getText().toUpperCase();
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
            allProductsTable.setItems(inventory.Inventory.getAllProducts());
        //otherwise, search
        else{
            FilteredList<Product> filteredProductsTable = new FilteredList<>(inventory.Inventory.getAllProducts(), product-> (product.getName().toUpperCase().contains(searchQuery) || product.getId() == finalSearchID));
            allProductsTable.setItems(filteredProductsTable);
        }
    }

    @FXML
    private void addProduct(ActionEvent event) {
        try{
            Stage addProductStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/inventory_add_product.fxml"));
            Scene addProductScene = new Scene(root);
            addProductStage.setScene(addProductScene);
            addProductStage.setTitle("Add Product");
            addProductStage.show();
        }
        catch(IOException e){
            System.out.println("didn't work\n" + e);
        }
    }

    @FXML
    private void modifyProduct(ActionEvent event) {
        try{
            FXMLLoader loader;
            if(allProductsTable.getSelectionModel().getSelectedItem() != null){
                loader = new FXMLLoader(getClass().getResource("../view/inventory_modify_product.fxml"));
            
                Parent root = loader.load();
                Inventory_modify_product_Controller controller = loader.getController();

                Product selectedProduct = (Product)allProductsTable.getSelectionModel().getSelectedItem();

                controller.setModProductId(selectedProduct.getId());
                controller.setModProductName(selectedProduct.getName());
                controller.setModProductInvQty(selectedProduct.getStock());
                controller.setModProductMaxQty(selectedProduct.getMax());
                controller.setModProductMinQty(selectedProduct.getMin());
                controller.setModProductPriceCost(selectedProduct.getPrice());

                int modIndex = inventory.Inventory.getProductIndex(selectedProduct);
                controller.setModIndex(modIndex);
                
                controller.setModParts(selectedProduct.getAllAssociatedParts());

                Scene modProductScene = new Scene(root);

                Stage modProductStage = new Stage();
                modProductStage.setScene(modProductScene);
                modProductStage.setTitle("Modify Product");
                modProductStage.show();
            }
            else{
                loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
                inventory.Inventory.openMessageDialog(loader, "Modify Product Failed","No product selected, modify operation failed");
            }
            
        }
        catch(IOException e){
            System.out.println("didn't work\n" + e);
        }
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        try{
            int id;
            Product target = (Product)allProductsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/inventory_message.fxml"));
            if (target != null &&
                target.getAllAssociatedParts().size() == 0 ){
                id = target.getId();
                inventory.Inventory.deleteProduct(target);
                inventory.Inventory.openMessageDialog(loader, "Delete Product Successful","Product (ID: " + id + ") deleted.");
            }
            else{
                if (target.getAllAssociatedParts().size() > 0)
                    inventory.Inventory.openMessageDialog(loader, "Delete Product Failed","Product has associated parts, please remove any associated parts before deleting the product.");
                else
                    inventory.Inventory.openMessageDialog(loader, "Delete Product Failed","No product selected, delete operation failed");
            }
        }
        catch (Exception e){
            //if loader fails to initialize...
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Stage stage = (Stage) mainExitBtn.getScene().getWindow();
        stage.close();
    }
    
}
