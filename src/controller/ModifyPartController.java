package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static java.lang.Integer.parseInt;

/** This class handles the operations between the classes in the model package and the ModifyPart.fxml component. */
public class ModifyPartController implements Initializable {
    @FXML
    RadioButton modifyInHousePart;
    @FXML
    RadioButton modifyOutsourcedPart;

    public TextField modifyPartIdField;
    public TextField modifyPartNameField;
    public TextField modifyPartStockField;
    public TextField modifyPartPriceField;
    public TextField modifyPartMinField;
    public TextField modifyPartMaxField;
    public TextField modifyPartMachineIdField;
    public Label modifyOriginLabel;
    public Label exceptionModifyPartLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /** This method detects a click on the cancel button and changes the stage to the Main Screen. */
    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not saved");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 950, 400);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** This method changes the text in the Label to Machine ID. */
    public void onInHouse(ActionEvent actionEvent) {
        modifyOriginLabel.setText("Machine ID");
    }

    /** This method changes the text in the Label to Company Name. */
    public void onOutsourced(ActionEvent actionEvent) {
        modifyOriginLabel.setText("Company Name");
    }

    /** This method brings all the data from the selected part that will be modified.
     @param part the part that is being modified
     */
    public void fetchPart(Part part) {
        modifyPartIdField.setText(String.valueOf(part.getId()));
        modifyPartNameField.setText(part.getName());
        modifyPartStockField.setText(String.valueOf(part.getStock()));
        modifyPartPriceField.setText(String.valueOf(part.getPrice()));
        modifyPartMinField.setText(String.valueOf(part.getMin()));
        modifyPartMaxField.setText(String.valueOf(part.getMax()));

        if (part instanceof InHouse) {
            modifyPartMachineIdField.setText(String.valueOf(((InHouse) part).getMachineId()));
            modifyOriginLabel.setText("Machine ID");
            modifyInHousePart.setSelected(true);
        }
        else if (part instanceof Outsourced) {
            modifyOriginLabel.setText("Company Name");
            modifyPartMachineIdField.setText(((Outsourced) part).getCompanyName());
            modifyOutsourcedPart.setSelected(true);
        }
    }

    /** This method saves the changes to the part that was modified */
    public void onSaveModifiedPart(ActionEvent actionEvent) throws IOException {
        String exceptionMessage = "";

        try {
            // Exception for Name Field not empty
            if(modifyPartNameField.getText().isEmpty()){
                exceptionMessage = exceptionMessage + "-Name field can't be empty\n";
                exceptionModifyPartLabel.setText(exceptionMessage);
            }
            else if (modifyInHousePart.isSelected() && parseInt(modifyPartStockField.getText()) >= parseInt(modifyPartMinField.getText()) && parseInt(modifyPartStockField.getText()) <= parseInt(modifyPartMaxField.getText()) && parseInt(modifyPartMaxField.getText()) >= parseInt(modifyPartMinField.getText())) {
                saveInModifiedHouse();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 950, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            } else if (modifyOutsourcedPart.isSelected() && parseInt(modifyPartStockField.getText()) >= parseInt(modifyPartMinField.getText()) && parseInt(modifyPartStockField.getText()) <= parseInt(modifyPartMaxField.getText()) && parseInt(modifyPartMaxField.getText()) >= parseInt(modifyPartMinField.getText())) {
                saveInModifiedOutsourced();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 950, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Dialog");
                alert.setContentText("The Maximum must be higher than the minimum and the Inventory must be in between both values");
                alert.showAndWait();
            }

        }catch (NumberFormatException e) {
            Object inputs[] = {
                    modifyPartNameField.getText(),
                    modifyPartStockField.getText(),
                    modifyPartPriceField.getText(),
                    modifyPartMinField.getText(),
                    modifyPartMaxField.getText(),
                    modifyPartMachineIdField.getText()
            };
            String inputNames[] = {
                    "-Name",
                    "-Inventory",
                    "-Price",
                    "-Minimum",
                    "-Maximum",
                    "-Machine ID or Company Name\n"
            };

            for (int i=1; i < inputs.length; i++) {
                if (inputs[i].equals("")) {
                    exceptionMessage = exceptionMessage + inputNames[i] + " field can't be empty \n";
                }
            }
            exceptionModifyPartLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Inventory field is an integer. */
        try {
            Integer.parseInt(modifyPartStockField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Inventory must be an integer\n";
            exceptionModifyPartLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Price field is a double. */
        try {
            Double.parseDouble(modifyPartPriceField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Price must be a double\n";
            exceptionModifyPartLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Minimum field is an integer. */
        try {
            Integer.parseInt(modifyPartMinField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Minimum must be an integer\n";
            exceptionModifyPartLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Maximum field is an integer. */
        try {
            Integer.parseInt(modifyPartMaxField.getText());
        } catch (NumberFormatException e) {
            exceptionMessage = exceptionMessage + "-Maximum must be an integer\n";
            exceptionModifyPartLabel.setText(exceptionMessage);
        }
    }

    /** This method saves the modified part as an In House part. */
    public void saveInModifiedHouse () {
        int id = 0;
        String name = null;
        int stock = 0;
        double price = 0;
        int min = 0;
        int max = 0;
        int machineId = 0;
        InHouse inHousePart = new InHouse(id, name, price, stock, min, max, machineId);
        inHousePart.setId(Integer.parseInt(modifyPartIdField.getText()));
        inHousePart.setName(modifyPartNameField.getText());
        inHousePart.setStock(Integer.parseInt(modifyPartStockField.getText()));
        inHousePart.setPrice(Double.parseDouble(modifyPartPriceField.getText()));
        inHousePart.setMin(Integer.parseInt(modifyPartMinField.getText()));
        inHousePart.setMax(Integer.parseInt(modifyPartMaxField.getText()));
        inHousePart.setMachineId(Integer.parseInt(modifyPartMachineIdField.getText()));
        Inventory.updatePart((Integer.parseInt(modifyPartIdField.getText())), inHousePart);
    }

    /** This method saves the modified part as an Outsourced part. */
    public void saveInModifiedOutsourced () {
        int id = 0;
        String name = null;
        double stock = 0;
        int price = 0;
        int min = 0;
        int max = 0;
        String companyName = null;
        Outsourced outsourced = new Outsourced(id, name, stock, price, min, max, companyName);
        outsourced.setId(Integer.parseInt(modifyPartIdField.getText()));
        outsourced.setName(modifyPartNameField.getText());
        outsourced.setStock(Integer.parseInt(modifyPartStockField.getText()));
        outsourced.setPrice(Double.parseDouble(modifyPartPriceField.getText()));
        outsourced.setMin(Integer.parseInt(modifyPartMinField.getText()));
        outsourced.setMax(Integer.parseInt(modifyPartMaxField.getText()));
        outsourced.setCompanyName(modifyPartMachineIdField.getText());
        Inventory.updatePart((Integer.parseInt(modifyPartIdField.getText())), outsourced);
    }
}
