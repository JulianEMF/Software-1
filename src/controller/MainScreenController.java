package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class handles the operations between the classes in the model package and the MainScreen.fxml component. */
public class MainScreenController implements Initializable{
    Stage stage;
    Parent scene;

    public TableView allPartsTable;
    public TableColumn partIdColumn;
    public TableColumn partNameColumn;
    public TableColumn partInventoryColumn;
    public TableColumn partPriceColumn;
    public TextField partQuery;
    public TableView allProductsTable;
    public TableColumn productIdColumn;
    public TableColumn productNameColumn;
    public TableColumn productInventoryColumn;
    public TableColumn productPriceColumn;
    public TextField productQuery;

    /** This method populates the table views */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        allPartsTable.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allProductsTable.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method detects a click on the Add Part button and changes the stage to the AddPart Screen. */
    public void toAddPart(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/addPart.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600,400);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /** This method detects a click on the Modify Part button and changes the stage to the ModifyPart Screen. */
    public void toModifyPart(ActionEvent actionEvent) throws IOException{
        Part part = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        if(part == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();

        ModifyPartController PartController = loader.getController();
        PartController.fetchPart(part);

        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene,  600, 400));
        stage.show();
    }

    /** This method detects a click on the Add Product button and changes the stage to the AddProduct Screen. */
    public void toAddProduct(ActionEvent actionEvent) throws IOException{
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method detects a click on the Modify Product button and changes the stage to the ModifyProduct Screen. */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException{
        Product productToModify = (Product) allProductsTable.getSelectionModel().getSelectedItem();
        if(productToModify == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        loader.load();

        ModifyProductController ProductController = loader.getController();
        ProductController.fetchProduct(productToModify);

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This method calls the addTestData method. */
    static{
        addTestData();
    }

    /** This method populates the tables with starting data. */
    public static void addTestData() {
        ObservableList<Part> associatedPartstoTricycle = FXCollections.observableArrayList();
        ObservableList<Part> associatedPartstoCar = FXCollections.observableArrayList();

        InHouse brakes = new InHouse(1, "Brakes", 34.99, 12, 10, 200, 10);
        Inventory.addPart(brakes);

        InHouse door = new InHouse(2, "Door", 250.49, 10, 5, 20, 12);
        Inventory.addPart(door);

        Outsourced seat = new Outsourced(3, "Seat", 48.49, 10, 10, 25, "Seats S.A");
        Inventory.addPart(seat);

        Outsourced wheel = new Outsourced(4, "Wheel", 75, 10, 6, 25, "Wheels S.A");
        Inventory.addPart(wheel);

        associatedPartstoTricycle.add(wheel);
        associatedPartstoCar.add(door);
        associatedPartstoCar.add(brakes);

        Product tricycle = new Product(1001, "Tricycle", 399.99, 10, 4, 20, associatedPartstoTricycle);
        Inventory.addProduct(tricycle);

        Product car = new Product(1002, "Car", 19900,6,5, 10, associatedPartstoCar);
        Inventory.addProduct(car);
    }

    /** This method handles the search function for parts*/
    public void getPartsResultsHandler(ActionEvent actionEvent) throws IOException{
        String query = partQuery.getText();
        ObservableList<Part> filteredParts = Inventory.lookUpPart(query);

        if(filteredParts.size() == 0){
            try {
                int id = Integer.parseInt(query);
                Part partName = Inventory.lookUpPart(id);
                if (partName != null) {
                    filteredParts.add(partName);
                }
            }catch(NumberFormatException e){
                //IGNORE
            }
        }if(filteredParts.size() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Parts Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The term " + query + " did not produce any results");
            alert.showAndWait();
        }
        allPartsTable.setItems(filteredParts);
        partQuery.setText("");
    }

    /** This method handles the search function for products*/
    public void getProductsResultsHandler(ActionEvent actionEvent) throws IOException{
        String query = productQuery.getText();
        ObservableList<Product> filteredProducts = Inventory.lookUpProduct(query);

        if(filteredProducts.size() == 0){
            try {
                int id = Integer.parseInt(query);
                Product productName = Inventory.lookUpProduct(id);
                if (productName != null) {
                    filteredProducts.add(productName);
                }
            }catch(NumberFormatException e){
                //IGNORE
            }
        }if(filteredProducts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Products Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The term " + query + " did not produce any results");
            alert.showAndWait();
        }
        allProductsTable.setItems(filteredProducts);
        productQuery.setText("");
    }

    /** This method deletes a selected part. */
    public void deletePartHandler(ActionEvent actionEvent) throws IOException{
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Parts");
        alert.setHeaderText("Delete " + selectedPart.getName());
        alert.setContentText("Are you sure you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
            allPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /** This method deletes a selected product. */
    public void deleteProductHandler(ActionEvent actionEvent) throws IOException{
        Product selectedProduct = (Product) allProductsTable.getSelectionModel().getSelectedItem();

        ObservableList<Part> productAssociatedParts = selectedProduct.getAllAssociatedParts();

        if(productAssociatedParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Products");
            alert.setHeaderText("Delete " + selectedProduct.getName());
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                allProductsTable.setItems(Inventory.getAllProducts());
            }
        }
        else{
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Products");
            warningAlert.setContentText("The product " + selectedProduct.getName() + " has associated parts so it can't be removed");
            warningAlert.showAndWait();
        }



//
    }

    /** This method closes the application. */
    public void onActionExitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will exit the Inventory Management application");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
}
