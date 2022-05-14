/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;
import controller.Inventory_message_Controller;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Dave
 */
public class Inventory extends Application{

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int seqPartId = 0;
    private static int seqProdId = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        
        allParts.add(new InHouse(getNewPartID(), "Sample In-House 1", 1.00, 10, 1, 20, 15));
        setNewPartID();
        allParts.add(new InHouse(getNewPartID(), "Sample In-House 2", 1.00, 5, 1, 20, 15));
        setNewPartID();
        allParts.add(new Outsourced(getNewPartID(), "Sample Outsourced 1", 1.00, 21, 10, 35, "Key Technology"));
        setNewPartID();
        
        
        allProducts.add(new Product(getNewProdID(), "Sample Product 1", 20.00, 5, 1, 10, FXCollections.observableArrayList(allParts)));
        setNewProdID();
        launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        String viewPath = "../view/inventory_main.fxml";
        
//        FXMLLoader loader = new FXMLLoader();
//        FileInputStream fxmlStream = new FileInputStream(controllerPath);
        
        //Parent root = loader.load(fxmlStream);
        Parent root = FXMLLoader.load(getClass().getResource(viewPath));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Inventory Manager");
        primaryStage.show();
    }
    
    public void exit() throws Exception {
        
    }
    
    
    
    
    /**
     * @param newPart the part to add
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    /**
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    
    /**
     * @param partId the partId to look up.
     * @return the part with the specified partId, if it exists.
     */
    public static Part lookupPart(int partId){
        
        Part curPart;
        Iterator<Part> itr = allParts.iterator();
        
        while (itr.hasNext()){
            curPart = itr.next();
            if (curPart.getId() == partId){
                return curPart;
            }
        }
        return null;
    }
    
    /**
     * @param productId the productId to look up.
     * @return the product with the specified productId, if it exists.
     */
    public static Product lookupProduct(int productId){
        
        Product curProd;
        Iterator<Product> itr = allProducts.iterator();
        
        while (itr.hasNext()){
            curProd = itr.next();
            if (curProd.getId() == productId){
                return curProd;
            }
        }
        return null;
    }
    
    /**
     * @param partName the partName to look up.
     * @return the list of parts matching the specified partName.
     */
    public static ObservableList<Part> lookupPart(String partName){
        
        ObservableList<Part> results = allParts;
        
        //Ensure supplied 'name' is not empty string.
        if (!partName.equals(""))
            results.filtered(p -> p.getName().contains(partName));

        return results;
    }
    
    /**
     * @param productName the productName to look up.
     * @return the list of products matching the specified productName.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        
        ObservableList<Product> results = allProducts;
        
        //Ensure supplied 'name' is not empty string.
        if (!productName.equals(""))
            results.filtered(p -> p.getName().contains(productName));
        
        return results;
    }
    
    /**
     * @param index the index of the part to update.
     * @param selectedPart the part information to update.
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }
    
    /**
     * @param index the index of the product to update.
     * @param selectedProduct the product information to update.
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }
    
    /**
     * @param selectedPart the part to delete
     * @return the success status of the deletion.
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }
    
    /**
     * @param selectedProduct the product to delete
     * @return the success status of the deletion.
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }
    
    /**
     * @return the list of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    
    /**
     * @return the list of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
    
    /**
     * @return next Part ID in the sequence
     */
    public static int getNewPartID(){
        return seqPartId;
    }
    
    /**
     * set the Part ID sequence to new value.
     * Only called when part successfully created/saved.
     */
    public static void setNewPartID(){
        seqPartId++;
    }
    
    /**
     * @return next Product ID in the sequence
     */
    public static int getNewProdID(){
        return seqProdId;
    }
    
    /**
     * set the Product ID sequence to new value.
     * Only called when product successfully created/saved.
     */
    public static void setNewProdID(){
        seqProdId++;
    }
    
    public static int isValid(String name, int invQty, int maxQty, int minQty, double priceCost, int machineId, String company, boolean isInHouse){
        
        //'Business Logic'
        if ( name.equals(""))
            return 1; //Null name error
        else if (   invQty < 0 ||
                    maxQty < 0 ||
                    minQty < 0 ||
                    maxQty < minQty ||
                    invQty < minQty ||
                    invQty > maxQty
                )
            return 2; //part qty error
        else if ( priceCost < 0.00)
            return 3; // price cost error
        else{
            if(isInHouse){
                if (machineId < 0 )
                    return 4; //machine ID error (cant be negative)
            }
            else{
                if(company.equals(""))
                    return 5; //null Company name error
            }
        }
        return 0;
    }
    
    /**
     * @param err the Error integer to transform to error text.
     * @return Error text for supplied Error integer.
     */
    public static String isValidText(int err){
        switch (err){
            case 1:
                return "Please enter Part name";
            case 2:
                return "Check qty values and bounds";
            case 3:
                return "Price cannot be negative";
            case 4:
                return "Machine ID cannot be negative";
            case 5:
                return "Please enter Company name";
            default:
                return "";
        }
    }
    
    public static int getPartIndex(Part selectedPart){
        return allParts.indexOf(selectedPart);
    }
    
    public static int getProductIndex(Product selectedProduct){
        return allProducts.indexOf(selectedProduct);
    }
    
    public static void openMessageDialog(FXMLLoader loader, String messageTitle, String messageText){
        try{
            Parent root = loader.load();
            Inventory_message_Controller controller = loader.getController();

            Scene messageScene = new Scene(root);
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);

            controller.setMessageTitle(messageTitle);
            controller.setMessageText(messageText);
            messageStage.show();
        }
        catch (IOException ioe){

        }
    }
    /**
     * Future Work:
     * 
     * Persistent data memory/Database:
     * Clearly, the functionality of the program is demonstrated, but something
     * like this would not be very useful without being able to save one's work.
     * One next step in this development would be to include a database functionality
     * or a text file read/write system to save the parts and products that are 
     * added and modified.
     * 
     * Better interconnection of objects:
     * Currently, once a part is added to a product, that part exists as a copy 
     * of the original part. If the original part is modified in the list of all
     * products, the analogous part that is associated with a product is not updated.
     * This could also be solved with the addition of a proper relational database.
     * Also, we would need to address weather to prevent a user from deleting a 
     * part that is associated with a product, or if we should allow this and 
     * cascade the change by deleting it from all products of which it is a member.
     */
}
