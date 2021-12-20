package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.util.Optional;
import static java.lang.Integer.parseInt;

/** This class handles the operations between the classes in the model package and the AddPart.fxml component. */
public class AddPartController {
    public Label originLabel;
    public RadioButton inHousePart;
    public RadioButton outsourcedPart;
    public TextField partIdField;
    public TextField partNameField;
    public TextField partInventoryField;
    public TextField partPriceField;
    public TextField partMinField;
    public TextField partMaxField;
    public TextField partMachineIdField;
    public Label exceptionLabel;

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
        originLabel.setText("Machine ID");
    }

    /** This method changes the text in the Label to Company Name. */
    public void onOutsourced(ActionEvent actionEvent) {
        originLabel.setText("Company Name");
    }

    /** This method generates IDs for new parts, iterating over the existing IDs and selecting the smallest possible value.
     @return newId
     */
    public static int partIdGenerator(){
        int newId = 1;
        for(int i=1; i<1000; i++){
            if (Inventory.lookUpPart(i) == null){
                break;
            }
            newId = i+1;
        }
        return newId;
    }


    /**
     RUNTIME ERROR:
     While working on this project I encountered several runtime errors. One of them was when trying to fill out the price field with letters, instead of numbers.
     The way I handled this error was by creating a try and catch statement that would try to convert the input into a double. To take care of this problem I
     created an array of Objects that contains the input from every field, then created a for loop that iterated over it checking if any field was empty. If they were,
     the name of that field was matched with an array of Strings that contains the name of the labels of each field. Combined with a String called exceptionMessage every error
     message is added at the end of that String and then its content is set into the exceptions label.
    This method handles the saving process for a new part. */
    public void savePartHandler(ActionEvent actionEvent) throws IOException {
        String exceptionMessage = "";
        try{
            // Exception for Name Field not empty
            if(partNameField.getText().isEmpty()){
                exceptionMessage = exceptionMessage + "-Name field can't be empty\n";
                exceptionLabel.setText(exceptionMessage);
            }
            if(inHousePart.isSelected() && parseInt(partInventoryField.getText()) >= parseInt(partMinField.getText()) &&
            parseInt(partInventoryField.getText()) <= parseInt(partMaxField.getText()) && parseInt(partMaxField.getText()) >= parseInt(partMinField.getText())){
                saveInHouse();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 950, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            }
            else if(outsourcedPart.isSelected() && parseInt(partInventoryField.getText()) >= parseInt(partMinField.getText()) &&
            parseInt(partInventoryField.getText()) <= parseInt(partMaxField.getText()) && parseInt(partMaxField.getText()) >= parseInt(partMinField.getText())){
                saveOutsourced();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 950, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Dialog");
                alert.setContentText("The Maximum must be higher than the minimum and the Inventory must be in between both values.");
                alert.showAndWait();
            }
        }catch(NumberFormatException e){
            Object inputs[] = {
                    partNameField.getText(),
                    partInventoryField.getText(),
                    partPriceField.getText(),
                    partMinField.getText(),
                    partMaxField.getText(),
                    partMachineIdField.getText()
            };
            String inputNames[] = {
                    "-Name",
                    "-Inventory",
                    "-Price",
                    "-Minimum",
                    "-Maximum",
                    "-Machine ID or Company Name\n"
            };
            for(int i=1; i<inputs.length; i++){
                if(inputs[i].equals("")){
                    exceptionMessage = exceptionMessage + inputNames[i] + " field can't be empty \n";
                }
            }
            exceptionLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Inventory field is an integer. */
        try{
            Integer.parseInt(partInventoryField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Inventory must be an integer\n";
            exceptionLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Price field is a double. */
        try{
            Double.parseDouble(partPriceField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Price must be a double\n";
            exceptionLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Minimum field is an integer */
        try{
            Integer.parseInt(partMinField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Minimum must be an integer\n";
            exceptionLabel.setText(exceptionMessage);
        }

        /** This exception validates that the input in the Maximum field is an integer */
        try{
            Integer.parseInt(partMaxField.getText());
        }catch(NumberFormatException e){
            exceptionMessage = exceptionMessage + "-Maximum must be an integer\n";
            exceptionLabel.setText(exceptionMessage);
        }
    }

    /** This method saves the new part as an In House part. */
    public void saveInHouse() {
        int id = partIdGenerator();
        String name = partNameField.getText();
        int inv = parseInt(partInventoryField.getText());
        Double price = Double.parseDouble(partPriceField.getText());
        int max = parseInt(partMaxField.getText());
        int min = parseInt(partMinField.getText());
        int machineId = parseInt(partMachineIdField.getText());
        Inventory.addPart(new InHouse(id, name, price, inv, min, max, machineId));
    }

    /** This method saves the new part as an Outsourced part. */
    public void saveOutsourced() {
        int id = partIdGenerator();
        String name = partNameField.getText();
        int inv = parseInt(partInventoryField.getText());
        Double price = Double.parseDouble(partPriceField.getText());
        int max = parseInt(partMaxField.getText());
        int min = parseInt(partMinField.getText());
        String companyName = partMachineIdField.getText();
        Inventory.addPart(new Outsourced(id, name, price, inv, min, max, String.valueOf(companyName)));
    }
}
