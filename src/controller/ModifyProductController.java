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

/** This class handles the operations between the classes in the model package and the ModifyProduct.fxml component. */
public class ModifyProductController implements Initializable {
    public TextField modifyProductPartQuery;
    public TextField modifyProductIdField;
    public TextField modifyProductNameField;
    public TextField modifyProductStockField;
    public TextField modifyProductPriceField;
    public TextField modifyProductMinField;
    public TextField modifyProductMaxField;
    public TableView modifyProductPartsTable;
    public TableColumn modifyProductPartIdColumn;
    public TableColumn modifyProductNameColumn;
    public TableColumn modifyProductStockColumn;
    public TableColumn modifyProductPriceColumn;
    public TableView modifyProductAssociatedPartsTable;
    public TableColumn modifyAssociatedPartIdColumn;
    public TableColumn modifyAssociatedNameColumn;
    public TableColumn modifyAssociatedStockColumn;
    public TableColumn modifyAssociatedPriceColumn;
    public Label exceptionModifyProduct;

    Stage stage;
    Parent scene;
    Product product;

    /** This method populates the existing parts table and the associated parts table. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        product = new Product();

        modifyProductPartsTable.setItems(Inventory.getAllParts());
        modifyProductAssociatedPartsTable.setItems(product.getAllAssociatedParts());
        modifyProductPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        modifyAssociatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyAssociatedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyAssociatedStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyAssociatedPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
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

    /** This method brings all the data from the selected product that will be modified.
     @param productDetails the product that needs to be fetched for display
     */
    public void fetchProduct(Product productDetails){
        modifyProductIdField.setText(String.valueOf(productDetails.getId()));
        modifyProductNameField.setText(productDetails.getName());
        modifyProductStockField.setText(String.valueOf(productDetails.getStock()));
        modifyProductPriceField.setText(String.valueOf(productDetails.getPrice()));
        modifyProductMinField.setText(String.valueOf(productDetails.getMin()));
        modifyProductMaxField.setText(String.valueOf(productDetails.getMax()));
        product.getAllAssociatedParts().addAll(productDetails.getAllAssociatedParts());
    }

    /** This method searches for existing parts that can be added as associated parts. */
    public void onModifySearchPart(ActionEvent actionEvent) {
        String query = modifyProductPartQuery.getText();
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
        modifyProductPartsTable.setItems(filteredParts);
        modifyProductPartQuery.setText("");
    }

    /** This method adds a part as an associated part in the current product. */
    public void onModifyAddAssociatedPart(ActionEvent actionEvent) {
        Part part = (Part) modifyProductPartsTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            return;
        }else{
            product.addAssociatedPart(part);
        }
    }

    /** This method deletes an associated part from the current product. */
    public void onModifyDeleteAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) modifyProductAssociatedPartsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete this part from the list?");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            product.deleteAssociatedPart(selectedPart);
        }

    }

    /** This method saves the modified product. */
    public void onSaveModifiedProduct(ActionEvent actionEvent) throws IOException {
        String exceptionMessage = "";

        try {
            // Exception for Name Field not empty
            if (modifyProductNameField.getText().isEmpty()) {
                exceptionMessage = exceptionMessage + "-Name field can't be empty\n";
                exceptionModifyProduct.setText(exceptionMessage);
            } else if (Integer.parseInt(modifyProductStockField.getText()) >= Integer.parseInt(modifyProductMinField.getText()) &&
                    Integer.parseInt(modifyProductStockField.getText()) <= Integer.parseInt(modifyProductMaxField.getText())) {
                int stock = Integer.parseInt(modifyProductStockField.getText());
                int min = Integer.parseInt(modifyProductMinField.getText());
                int max = Integer.parseInt(modifyProductMaxField.getText());
                int id = Integer.parseInt(modifyProductIdField.getText());
                String name = modifyProductNameField.getText();
                double price = Double.parseDouble(modifyProductPriceField.getText());
                Product modifiedProduct = new Product(id, name, price, stock, min, max, product.getAllAssociatedParts());
                Inventory.updateProduct(id, modifiedProduct);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Dialog");
                alert.setContentText("The maximum must be higher than the minimum and the Inventory must be in between both values");
                alert.showAndWait();
            }
        }catch(NumberFormatException e){
            Object inputs[] = {
                    modifyProductNameField.getText(),
                    modifyProductStockField.getText(),
                    modifyProductPriceField.getText(),
                    modifyProductMinField.getText(),
                    modifyProductMaxField.getText()
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
            exceptionModifyProduct.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Inventory field is an integer. */
        try {
            Integer.parseInt(modifyProductStockField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Inventory must be an integer\n";
            exceptionModifyProduct.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Price field is a double. */
        try {
            Double.parseDouble(modifyProductPriceField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Price must be a double\n";
            exceptionModifyProduct.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Minimum field is an integer. */
        try {
            Integer.parseInt(modifyProductMinField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Minimum must be an integer\n";
            exceptionModifyProduct.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Maximum field is an integer. */
        try {
            Integer.parseInt(modifyProductMaxField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Maximum must be an integer\n";
            exceptionModifyProduct.setText(exceptionMessage);
        }
    }
}
