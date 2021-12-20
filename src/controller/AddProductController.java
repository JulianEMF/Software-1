package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class handles the operations between the classes in the model package and the AddProduct.fxml component. */
public class AddProductController implements Initializable{
    public TextField productPartQuery;
    public TableView productPartsTable;
    public TableColumn productPartIdColumn;
    public TableColumn productNameColumn;
    public TableColumn productStockColumn;
    public TableColumn productPriceColumn;
    public TableView productAssociatedPartsTable;
    public TableColumn associatedPartIdColumn;
    public TableColumn associatedNameColumn;
    public TableColumn associatedStockColumn;
    public TableColumn associatedPriceColumn;
    public TextField productIdField;
    public TextField productNameField;
    public TextField productStockField;
    public TextField productPriceField;
    public TextField productMinField;
    public TextField productMaxField;
    public Label addProductExceptions;

    Stage stage;
    Parent scene;
    Product product;

    /** This method populates the table views */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        product = new Product();

        productPartsTable.setItems(Inventory.getAllParts());
        productAssociatedPartsTable.setItems(product.getAllAssociatedParts());
        productPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method detects a click on the cancel button and changes the stage to the Main Screen. */
    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not saved");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This method generates IDs for new products, iterating over the existing IDs and selecting the smallest possible value.
     return newId
     */
    public static int productIdGenerator(){
        int newId = 1001;
        for(int i=1001; i<2000; i++){
            if (Inventory.lookUpProduct(i) == null){
                newId = i;
                break;
            }
        }
        return newId;
    }

    /** This method searches for existing parts that can be added as associated parts. */
    public void onSearchPart(ActionEvent actionEvent) {
        String query = productPartQuery.getText();
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
        productPartsTable.setItems(filteredParts);
        productPartQuery.setText("");
    }

    /** This method adds a part as an associated part in the current product. */
    public void onAddPart(ActionEvent actionEvent) {
        Part part = (Part) productPartsTable.getSelectionModel().getSelectedItem();
        if(part == null) {
            return;
        }
        else {
            product.addAssociatedPart(part);
        }
    }

    /** This method deletes an associated part. */
    public void onDeleteAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) productAssociatedPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will delete " + selectedPart.getName() + " from the associated part list.");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            product.deleteAssociatedPart(selectedPart);
        }
    }

    /** This method saves the new product */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {
        String exceptionMessage = "";
    try {
        // Exception for Name Field not empty
        if(productNameField.getText().isEmpty()){
            exceptionMessage = exceptionMessage + "-Name field can't be empty\n";
            addProductExceptions.setText(exceptionMessage);
        }

        if (Integer.parseInt(productStockField.getText()) >= Integer.parseInt(productMinField.getText()) &&
                Integer.parseInt(productStockField.getText()) <= Integer.parseInt(productMaxField.getText())) {
            int id = productIdGenerator();
            String name = productNameField.getText();
            int stock = Integer.parseInt(productStockField.getText());
            Double price = Double.parseDouble(productPriceField.getText());
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());
            Inventory.addProduct(new Product(id, name, price, stock, min, max, product.getAllAssociatedParts()));

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Dialog");
            alert.setContentText("Inventory must fall between the Min and Max Inventory!");
            alert.showAndWait();
        }
    }
    catch(NumberFormatException e){
        Object inputs[] = {
                productNameField.getText(),
                productStockField.getText(),
                productPriceField.getText(),
                productMinField.getText(),
                productMaxField.getText()
        };
        String inputNames[] = {
                "-Name",
                "-Inventory",
                "-Price",
                "-Minimum",
                "-Maximum"
        };
        for(int i=1; i<inputs.length; i++){
            if(inputs[i].equals("")){
                exceptionMessage = exceptionMessage + inputNames[i] + " field can't be empty \n";
            }
        }
        addProductExceptions.setText(exceptionMessage);
    }

        /** This exception validates that the input in the Inventory field is an integer. */
        try{
            Integer.parseInt(productStockField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Inventory must be an integer\n";
            addProductExceptions.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Price field is a double. */
        try{
            Double.parseDouble(productPriceField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Price must be a double\n";
            addProductExceptions.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Minimum field is an integer. */
        try{
            Integer.parseInt(productMinField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Minimum must be an integer\n";
            addProductExceptions.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Maximum field is an integer. */
        try{
            Integer.parseInt(productMaxField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Maximum must be an integer\n";
            addProductExceptions.setText(exceptionMessage);
        }
    }
}
