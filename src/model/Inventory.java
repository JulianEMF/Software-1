package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The Inventory class holds the observable lists that contain the parts and products. As well as the methods to edit them. */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** This method adds a new part to the allParts observable list.
     @param newPart the new part to be added
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /** This method adds a new product to the allProducts observable list.
     @param newProduct the new product to be added
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /** This method iterates over the allParts array looking for the matching Id.
     @param partId the id of the part
     @return p The matching Id
     */
    public static Part lookUpPart(int partId){
        for (Part p : allParts){
            if (p.getId() == partId){
                return p;
            }
        }
        return null;
    }

    /** This method iterates over the allProducts array looking for the matching Id.
     @param productId the id of the product
     @return p The matching Id
     */
    public static Product lookUpProduct(int productId){
        for (Product p : allProducts){
            if (p.getId() == productId){
                return p;
            }
        }
        return null;
    }

    /** This method iterates over the allParts array looking for the matching part name.
     @param partName the name of the part
     @return partNames
     */
    public static ObservableList<Part> lookUpPart(String partName){
        ObservableList<Part> partNames = FXCollections.observableArrayList();
        for (Part i : allParts){
            if(i.getName().contains(partName)){
                partNames.add(i);
            }
        }
        return partNames;
    }

    /** This method iterates over the allProducts array looking for the matching product name.
     @param productName the name of the product
     @return productNames
     */
    public static ObservableList<Product> lookUpProduct(String productName){
        ObservableList<Product> productNames = FXCollections.observableArrayList();
        for (Product i : allProducts){
            if(i.getName().contains(productName)){
                productNames.add(i);
            }
        }
        return productNames;
    }

    /** This method updates the selected part.
     @param index the part's index in the array
     @param selectedPart the selected part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index-1, selectedPart);
    }

    /** This method updates the selected product.
     @param index product's index in the array
     @param productName name of the product
     */
    public static void updateProduct(int index, Product productName){
        allProducts.set(index-1001, productName);
    }

    /** This method deletes the selected part.
     @param selectedPart the selected part
     @return false
     */
    public static boolean deletePart(Part selectedPart){
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.contains(selectedPart)) {
                allParts.remove(selectedPart);
            }
        }
        return false;
    }

    /** This method deletes the selected product.
     @param selectedProduct the selected product
     @return false
     */
    public static boolean deleteProduct(Product selectedProduct){
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.contains(selectedProduct)) {
                allProducts.remove(selectedProduct);
            }
        }
        return false;
    }

    /** This method returns all the parts.
     @return allParts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /** This method returns all the products.
     @return allProducts
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}